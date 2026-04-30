package com.clinica.limatambo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.clinica.limatambo.service.UsuarioService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomSuccessHandler successHandler;

    @Autowired
    private UsuarioService usuarioService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/css/**", "/js/**", "/img/**", "/paciente/registro").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/medico/**").hasRole("MEDICO")
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(successHandler)
                .permitAll()
            )
            
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}