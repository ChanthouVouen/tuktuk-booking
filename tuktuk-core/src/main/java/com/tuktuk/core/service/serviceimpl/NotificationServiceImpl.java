package com.tuktuk.core.service.serviceimpl;

import com.tuktuk.common.dto.NotificationResponse;
import com.tuktuk.core.mapper.NotificationMapper;
import com.tuktuk.core.service.NotificationService;
import com.tuktuk.domain.entity.Notification;
import com.tuktuk.domain.repository.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> findForPassenger(Long passengerId) {
        return toResponses(notificationRepository.findByPassengerIdOrderByCreatedAtDesc(passengerId));
    }

    @Override
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
