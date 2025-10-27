package co.edu.uniquindio.ingesis.notifications_service.repository.spec;

import co.edu.uniquindio.ingesis.notifications_service.entity.Channel;
import co.edu.uniquindio.ingesis.notifications_service.entity.Notification;
import co.edu.uniquindio.ingesis.notifications_service.entity.NotificationStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class NotificationSpecs {

    public static Specification<Notification> recipientEq(String recipient) {
        return (r, q, cb) -> recipient == null ? null :
                cb.equal(cb.lower(r.get("recipient")), recipient.toLowerCase());
    }

    public static Specification<Notification> statusEq(NotificationStatus status) {
        return (r, q, cb) -> status == null ? null : cb.equal(r.get("status"), status);
    }

    public static Specification<Notification> channelEq(Channel channel) {
        return (r, q, cb) -> channel == null ? null : cb.equal(r.get("channel"), channel);
    }

    public static Specification<Notification> createdBetween(Instant from, Instant to) {
        return (r, q, cb) -> {
            if (from == null && to == null) return null;
            if (from != null && to != null)   return cb.between(r.get("createdAt"), from, to);
            if (from != null)                 return cb.greaterThanOrEqualTo(r.get("createdAt"), from);
            return cb.lessThanOrEqualTo(r.get("createdAt"), to);
        };
    }

    // Búsqueda básica por título o mensaje
    public static Specification<Notification> searchLike(String q) {
        return (r, query, cb) -> {
            if (q == null || q.isBlank()) return null;
            String like = "%" + q.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(r.get("title")), like),
                    cb.like(cb.lower(r.get("message")), like)
            );
        };
    }
}