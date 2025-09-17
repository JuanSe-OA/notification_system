package co.edu.uniquindio.ingesis.notifications_service.service;

import co.edu.uniquindio.ingesis.notifications_service.entity.Notification;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsappSender implements ChannelSender {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.whatsapp-number}")
    private String fromWhatsapp;

    @Override
    public void send(Notification notification) throws Exception {
        try {
            Twilio.init(accountSid, authToken);

            Message.creator(
                    new com.twilio.type.PhoneNumber("whatsapp:" + notification.getRecipient()),
                    new com.twilio.type.PhoneNumber(fromWhatsapp),
                    notification.getMessage()
            ).create();

            System.out.println("✅ WhatsApp enviado a: " + notification.getRecipient());
        } catch (Exception e) {
            System.err.println("❌ Error enviando WhatsApp: " + e.getMessage());
            throw e;
        }
    }
}
