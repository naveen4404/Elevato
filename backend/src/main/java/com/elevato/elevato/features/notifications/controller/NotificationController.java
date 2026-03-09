package com.elevato.elevato.features.notifications.controller;


import com.elevato.elevato.features.authentication.model.AuthenticationUser;
import com.elevato.elevato.features.notifications.model.Notification;
import com.elevato.elevato.features.notifications.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;


    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<Notification> getNotifications(@RequestAttribute("authenticatedUser")AuthenticationUser user){
        return notificationService.getNotifications(user);
    }

    @PutMapping("/{notificationId}")
    public Notification markNotificationAsRead(@PathVariable Long notificationId){
        return notificationService.markNotificationAsRead(notificationId);
    }
}
