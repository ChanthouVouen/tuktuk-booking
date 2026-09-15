package com.tuktuk.core.service;

import com.tuktuk.common.dto.RatingCreateRequest;
import com.tuktuk.common.dto.RatingResponse;
import java.util.List;

public interface RatingService {

    RatingResponse create(Long passengerId, Long bookingId, RatingCreateRequest request);

    /** Ratings a driver has received from passengers, most recent first. */
    List<RatingResponse> findForDriver(Long driverId);

}
