package com.tuktuk.core.notification;

import com.tuktuk.core.notification.dto.NotificationResponse;
import com.tuktuk.domain.notification.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public List<NotificationResponse> findForPassenger(Long passengerId) {
        return toResponses(notificationRepository.findByPassengerIdOrderByCreatedAtDesc(passengerId));
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> findForDriver(Long driverId) {
        return toResponses(notificationRepository.findByDriverIdOrderByCreatedAtDesc(driverId));
    }

    private List<NotificationResponse> toResponses(List<com.tuktuk.domain.notification.Notification> notifications) {
        return notifications.stream()
                .map(notification -> NotificationResponse.builder()
                        .id(notification.getId())
                        .message(notification.getMessage())
                        .read(notification.isRead())
                        .createdAt(notification.getCreatedAt())
                        .build())
                .toList();
    }
}