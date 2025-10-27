package co.edu.uniquindio.ingesis.notifications_service.service;

import co.edu.uniquindio.ingesis.notifications_service.entity.Notification;
import co.edu.uniquindio.ingesis.notifications_service.entity.NotificationStatus;
import co.edu.uniquindio.ingesis.notifications_service.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;
    private final EmailSender emailSender;
    private final SmsSender smsSender;

    public Notification createNotification(Notification notification) {
        notification.setCreatedAt(Instant.now());
        notification.setStatus(NotificationStatus.PENDING);
        return repository.save(notification);
    }

    @Transactional
    @Scheduled(fixedDelay = 60000)
    public void sendScheduledNotifications() {
        Instant now = Instant.now();
        List<Notification> pendientes = repository.findByStatusAndScheduledAtBefore(NotificationStatus.PENDING, now);
        for (Notification notification : pendientes) {
            processNotification(notification);
            // Opcional: actualizar estado a ENVIANDO o similar
        }
    }

    public void processNotification(Notification notification) {
        try {
            switch (notification.getChannel()) {
                case EMAIL -> emailSender.send(notification);
                case SMS -> smsSender.send(notification);

            }
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(Instant.now());
            repository.save(notification);
        } catch (Exception e) {
            notification.setStatus(NotificationStatus.FAILED);
            notification.setFailureReason(e.getMessage());
            repository.save(notification);
        }
    }

}
