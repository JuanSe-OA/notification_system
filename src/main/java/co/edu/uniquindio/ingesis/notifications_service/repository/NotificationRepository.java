package co.edu.uniquindio.ingesis.notifications_service.repository;

import co.edu.uniquindio.ingesis.notifications_service.entity.Channel;
import co.edu.uniquindio.ingesis.notifications_service.entity.Notification;
import co.edu.uniquindio.ingesis.notifications_service.entity.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository
        extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification>
{
    Page<Notification> findByChannel(Channel channel, Pageable pageable);


    Page<Notification> findByStatus(NotificationStatus status, Pageable pageable);


    Optional<Notification> findById(Long id);

    List<Notification> findByStatusAndScheduledAtBefore(NotificationStatus status, Instant before);
}