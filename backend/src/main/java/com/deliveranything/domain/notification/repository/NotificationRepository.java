package com.deliveranything.domain.notification.repository;

import com.deliveranything.domain.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long profileId);

  List<Notification> findByRecipientIdAndIsReadOrderByCreatedAtDesc(Long profileId, boolean isRead);

  Optional<Notification> findByIdAndRecipientId(Long id, Long profileId);

  long countByRecipientIdAndIsReadFalse(Long profileId);
}

