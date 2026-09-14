package com.tuktuk.core.service;

import com.tuktuk.common.dto.NotificationResponse;
import java.util.List;

public interface NotificationService {

    List<NotificationResponse> findForPassenger(Long passengerId);

    List<NotificationResponse> findForDriver(Long driverId);

}
