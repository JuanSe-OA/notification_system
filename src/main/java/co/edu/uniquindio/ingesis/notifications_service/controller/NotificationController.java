package co.edu.uniquindio.ingesis.notifications_service.controller;

import co.edu.uniquindio.ingesis.notifications_service.entity.Notification;
import co.edu.uniquindio.ingesis.notifications_service.repository.NotificationRepository;
import co.edu.uniquindio.ingesis.notifications_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;
    @GetMapping("/channels")
    public ResponseEntity<List<String>> getChannels() {
        List<String> channels = List.of("EMAIL", "SMS", "WHATSAPP");
        return ResponseEntity.ok(channels);
    }

    // Enviar notificación (crear)
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification created = notificationService.createNotification(notification);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotification(@PathVariable Long id) {
        return notificationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
