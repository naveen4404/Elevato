package com.elevato.elevato.features.notifications.repository;

import com.elevato.elevato.features.authentication.model.AuthenticationUser;
import com.elevato.elevato.features.notifications.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> findByRecipient(AuthenticationUser recipient);

    List<Notification> findByRecipientOrderByCreatedAtDesc(AuthenticationUser recipient);
}
