package co.edu.uniquindio.ingesis.notifications_service.service;

import co.edu.uniquindio.ingesis.notifications_service.entity.Notification;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailSender implements ChannelSender {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void send(Notification notification) throws Exception {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notification.getRecipient());
            message.setSubject(notification.getTitle());
            message.setText(notification.getMessage());

            mailSender.send(message);
            System.out.println("✅ Email enviado a: " + notification.getRecipient());
        } catch (Exception e) {
            System.err.println("❌ Error enviando email: " + e.getMessage());
            throw e;
        }
    }
}
