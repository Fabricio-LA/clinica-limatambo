# Etapa de construcción (Build)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos el archivo de configuración de Maven
COPY pom.xml .

# Descargamos las dependencias (esto mejora la caché de Docker)
RUN mvn dependency:go-offline

# Copiamos el código fuente
COPY src ./src

# Compilamos el proyecto omitiendo los tests para mayor velocidad
RUN mvn clean package -DskipTests

# Etapa de ejecución (Run)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiamos el .jar compilado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
