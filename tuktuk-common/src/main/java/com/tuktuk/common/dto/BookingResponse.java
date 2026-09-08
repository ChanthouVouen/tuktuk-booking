package com.tuktuk.common.dto;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookingResponse {

    private Long id;
    private PassengerResponse passenger;
    private DriverResponse driver;
    private BigDecimal pickupLat;
    private BigDecimal pickupLong;
    private BigDecimal dropLat;
    private BigDecimal dropLong;
    private BigDecimal distanceKm;
    private BigDecimal price;
    private String status;
    private Long vehicleTypeId;
    private String vehicleTypeName;
    private Instant createdAt;
    private Instant updatedAt;

}
