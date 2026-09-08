package com.tuktuk.core.mapper;

import com.tuktuk.common.dto.RatingResponse;
import com.tuktuk.domain.entity.Rating;

public final class RatingMapper {

    private RatingMapper() {
    }

    public static RatingResponse toResponse(Rating rating) {
        return RatingResponse.builder()
                .id(rating.getId())
                .bookingId(rating.getBooking().getId())
                .driverId(rating.getDriver().getId())
                .passengerId(rating.getPassenger().getId())
                .score(rating.getScore())
                .comment(rating.getComment())
                .createdAt(rating.getCreatedAt())
                .build();
    }

}
