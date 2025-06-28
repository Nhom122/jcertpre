package com.jcertpre.repositories;

import com.jcertpre.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface INotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByType(String type);
}