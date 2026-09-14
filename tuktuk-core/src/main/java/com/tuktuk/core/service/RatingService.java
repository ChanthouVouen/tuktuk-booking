package com.tuktuk.core.service;

import com.tuktuk.common.dto.RatingCreateRequest;
import com.tuktuk.common.dto.RatingResponse;

public interface RatingService {

    RatingResponse create(Long passengerId, Long bookingId, RatingCreateRequest request);

}
