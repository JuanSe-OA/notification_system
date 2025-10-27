package co.edu.uniquindio.ingesis.notifications_service.controller;

import co.edu.uniquindio.ingesis.notifications_service.entity.Channel;
import co.edu.uniquindio.ingesis.notifications_service.entity.Notification;
import co.edu.uniquindio.ingesis.notifications_service.entity.NotificationStatus;
import co.edu.uniquindio.ingesis.notifications_service.repository.NotificationRepository;
import co.edu.uniquindio.ingesis.notifications_service.repository.spec.NotificationSpecs;
import co.edu.uniquindio.ingesis.notifications_service.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Endpoints de notificaciones")
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
    @GetMapping
    public ResponseEntity<Page<Notification>> list(
            @RequestParam(required = false) String recipient,
            @RequestParam(required = false) NotificationStatus status,
            @RequestParam(required = false) Channel channel,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            @RequestParam(required = false, name = "q") String search
    ) {
        Sort sortObj = parseSort(sort, "createdAt", Sort.Direction.DESC);
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Specification<Notification> spec = (root, query, cb) -> cb.conjunction();

        spec = spec.and(NotificationSpecs.recipientEq(recipient));
        spec = spec.and(NotificationSpecs.statusEq(status));
        spec = spec.and(NotificationSpecs.channelEq(channel));
        spec = spec.and(NotificationSpecs.createdBetween(from, to));
        spec = spec.and(NotificationSpecs.searchLike(search));

        Page<Notification> result = notificationRepository.findAll(spec, pageable);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/me")
    @Operation(
            summary = "Mis notificaciones",
            description = "Devuelve el historial paginado de notificaciones del usuario autenticado. Filtro opcional por canal.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", useReturnTypeSchema = true),
                    @ApiResponse(responseCode = "401", description = "No autorizado")
            }
    )
    public ResponseEntity<Page<Notification>> myNotifications(
            Authentication authentication,
            @RequestParam(required = false) Channel channel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        String email = authentication.getName(); // del JwtTokenFilter (asegúrate que sea el email)
        Pageable pageable = PageRequest.of(page, size);

        Page<Notification> result = (channel == null)
                ? notificationRepository.findByRecipientIgnoreCaseOrderByCreatedAtDesc(email, pageable)
                : notificationRepository.findByRecipientIgnoreCaseAndChannelOrderByCreatedAtDesc(email, channel, pageable);

        return ResponseEntity.ok(result);
    }

    private Sort parseSort(String sortParam, String defaultProp, Sort.Direction defaultDir) {
        try {
            String[] parts = sortParam.split(",");
            String prop = parts[0];
            Sort.Direction dir = (parts.length > 1) ? Sort.Direction.fromString(parts[1]) : defaultDir;
            return Sort.by(dir, prop);
        } catch (Exception e) {
            return Sort.by(defaultDir, defaultProp);
        }
    }



}
