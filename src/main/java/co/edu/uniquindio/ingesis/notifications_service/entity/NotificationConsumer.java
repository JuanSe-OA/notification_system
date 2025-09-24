package co.edu.uniquindio.ingesis.notifications_service.entity;

import co.edu.uniquindio.ingesis.notifications_service.config.RabbitConfig;
import co.edu.uniquindio.ingesis.notifications_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitConfig.NOTIFICATION_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    public void receiveNotification(Notification notification) {
        // Aquí procesas la notificación, por ejemplo, enviar email o SMS
        notificationService.processNotification(notification);
    }
}
