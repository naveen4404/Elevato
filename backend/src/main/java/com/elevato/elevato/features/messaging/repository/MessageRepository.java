package com.elevato.elevato.features.messaging.repository;

import com.elevato.elevato.features.messaging.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
