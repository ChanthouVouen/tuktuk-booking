package com.tuktuk.core.passenger.dto;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PassengerResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private Instant createdAt;
    private Instant updatedAt;

}
