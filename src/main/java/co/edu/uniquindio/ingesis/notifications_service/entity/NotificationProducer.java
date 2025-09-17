package co.edu.uniquindio.ingesis.notifications_service.entity;

import co.edu.uniquindio.ingesis.notifications_service.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendNotification(Notification notification) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.NOTIFICATION_EXCHANGE,
                RabbitConfig.NOTIFICATION_ROUTING_KEY,
                notification
        );
    }
}
