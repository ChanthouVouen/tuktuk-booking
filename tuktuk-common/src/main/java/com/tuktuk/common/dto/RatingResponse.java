package com.tuktuk.common.dto;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RatingResponse {

    private Long id;
    private Long bookingId;
    private Long driverId;
    private Long passengerId;
    private Integer score;
    private String comment;
    private Instant createdAt;
}
