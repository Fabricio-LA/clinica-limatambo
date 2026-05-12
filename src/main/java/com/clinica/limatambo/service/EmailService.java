package com.clinica.limatambo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoRecuperacion(String destinatario, String enlaceRecuperacion) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject("Recuperación de Contraseña - Clínica Limatambo");
        mensaje.setText("Hola,\n\n" +
                "Has solicitado recuperar tu contraseña. Por favor, haz clic en el siguiente enlace para restablecerla:\n"
                +
                enlaceRecuperacion + "\n\n" +
                "Si no solicitaste esto, puedes ignorar este correo.\n\n" +
                "Atentamente,\n" +
                "Clínica Limatambo");

        mailSender.send(mensaje);
    }
}


