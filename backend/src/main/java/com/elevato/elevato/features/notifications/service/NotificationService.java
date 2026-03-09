package com.elevato.elevato.features.notifications.service;

import com.elevato.elevato.exception.ResourceNotFoundException;
import com.elevato.elevato.features.authentication.model.AuthenticationUser;
import com.elevato.elevato.features.feed.model.Comment;
import com.elevato.elevato.features.notifications.model.Notification;
import com.elevato.elevato.features.notifications.model.NotificationType;
import com.elevato.elevato.features.notifications.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public List<Notification> getNotifications(AuthenticationUser user) {
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(user);
    }

    public Notification markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(()-> new ResourceNotFoundException("Notification not found."));
        notification.setRead(true);
        messagingTemplate.convertAndSend("/topic/users/"+notification.getRecipient().getId()+"/notifications",notification);
        return notificationRepository.save(notification);
    }

    public void sendLikeNotification(AuthenticationUser actor, AuthenticationUser recipient, Long resourceId) {
        if(actor.getId().equals(recipient.getId())){
            return;
        }
        Notification notification = new Notification(actor,recipient, NotificationType.LIKE, resourceId);
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/users/"+recipient.getId()+"/notifications",notification);
    }


    public void sendCommentNotification(AuthenticationUser actor, AuthenticationUser recipient, Long resourceId) {
        if(actor.getId().equals(recipient.getId())){
            return;
        }
        Notification notification = new Notification(actor,recipient, NotificationType.COMMENT, resourceId);
        notificationRepository.save(notification);
        messagingTemplate.convertAndSend("/topic/users/"+recipient.getId()+"/notifications",notification);
    }

    public void sendLikesToPost(Long postId, Set<AuthenticationUser> likes){
        messagingTemplate.convertAndSend("/topic/likes/"+postId,likes);
    }

    public void sendCommentToPost(Long postId, Comment comment){
        messagingTemplate.convertAndSend("/topic/comments/"+postId,comment);
    }

    public void sendDeleteCommentToPost(Long postId, Comment comment){
        messagingTemplate.convertAndSend("/topic/comments/"+postId+"/delete",comment);
    }
}
