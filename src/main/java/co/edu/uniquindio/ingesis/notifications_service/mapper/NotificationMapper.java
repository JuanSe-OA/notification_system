package co.edu.uniquindio.ingesis.notifications_service.mapper;

import co.edu.uniquindio.ingesis.notifications_service.dto.NotificationResponse;
import co.edu.uniquindio.ingesis.notifications_service.entity.Notification;

public class NotificationMapper {
    public static NotificationResponse toDto(Notification n) {
        return new NotificationResponse(
                n.getId(),
                n.getTitle(),
                n.getMessage(),
                n.getRecipient(),
                n.getChannel(),
                n.getStatus(),
                n.getCreatedAt(),
                n.getScheduledAt(),
                n.getSentAt(),
                n.getFailureReason()
        );
    }
}