package co.edu.uniquindio.ingesis.notifications_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notifications")
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;


    @Column(length = 4000)
    private String message;


    private String recipient;


    @Enumerated(EnumType.STRING)
    private Channel channel;


    @Enumerated(EnumType.STRING)
    private NotificationStatus status;


    private Instant scheduledAt;
    private Instant createdAt;
    private Instant sentAt;

    private String failureReason;

}
