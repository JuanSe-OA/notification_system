package co.edu.uniquindio.ingesis.notifications_service.dto;

import co.edu.uniquindio.ingesis.notifications_service.entity.Channel;
import co.edu.uniquindio.ingesis.notifications_service.entity.NotificationStatus;

import java.time.Instant;

public record NotificationResponse(
        Long id,
        String title,
        String message,
        String recipient,
        String phoneNumber,
        Channel channel,
        NotificationStatus status,
        Instant createdAt,
        Instant scheduledAt,
        Instant sentAt,
        String failureReason
) {
}
