package co.edu.uniquindio.ingesis.notifications_service.service;

import co.edu.uniquindio.ingesis.notifications_service.entity.Notification;

public interface ChannelSender {
    void send(Notification notification) throws Exception;
}