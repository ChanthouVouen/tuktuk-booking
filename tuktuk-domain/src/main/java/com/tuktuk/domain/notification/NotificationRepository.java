package com.tuktuk.domain.notification;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByPassengerIdOrderByCreatedAtDesc(Long passengerId);

    List<Notification> findByDriverIdOrderByCreatedAtDesc(Long driverId);
}