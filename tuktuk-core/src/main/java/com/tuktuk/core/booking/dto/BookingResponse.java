package com.tuktuk.core.booking.dto;

import com.tuktuk.domain.booking.BookingStatus;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookingResponse {

    private Long id;
    private Long passengerId;
    private Long driverId;
    private Long vehicleId;
    private String vehicleName;
    private String vehicleType;
    private BigDecimal pickupLat;
    private BigDecimal pickupLong;
    private BigDecimal dropLat;
    private BigDecimal dropLong;
    private BigDecimal distanceKm;
    private BigDecimal price;
    private BookingStatus status;
    private Long vehicleTypeId;
    private String vehicleTypeName;
    private Instant createdAt;
    private Instant updatedAt;

}
