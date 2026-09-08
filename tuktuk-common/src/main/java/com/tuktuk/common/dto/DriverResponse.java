package com.tuktuk.common.dto;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DriverResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String licenseNumber;
    private String vehiclePlate;
    private Instant createdAt;
    private Instant updatedAt;

}
