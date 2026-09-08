package com.tuktuk.core.service;

import com.tuktuk.common.dto.NotificationResponse;
import com.tuktuk.core.mapper.NotificationMapper;
import com.tuktuk.domain.entity.Notification;
import com.tuktuk.domain.repository.NotificationRepository;
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

    private List<NotificationResponse> toResponses(List<Notification> notifications) {
        return notifications.stream()
                .map(NotificationMapper::toResponse)
                .toList();
    }
}
