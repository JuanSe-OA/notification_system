package co.edu.uniquindio.ingesis.notifications_service.service;

import co.edu.uniquindio.ingesis.notifications_service.entity.Notification;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsSender implements ChannelSender {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String fromPhone;

    @Override
    public void send(Notification notification) throws Exception {
        try {
            Twilio.init(accountSid, authToken);

            Message.creator(
                    new com.twilio.type.PhoneNumber(notification.getPhoneNumber()),
                    new com.twilio.type.PhoneNumber(fromPhone),
                    notification.getMessage()
            ).create();

            System.out.println("✅ SMS enviado a: " + notification.getPhoneNumber());
        } catch (Exception e) {
            System.err.println("❌ Error enviando SMS: " + e.getMessage());
            throw e;
        }
    }
}
