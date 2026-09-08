package com.tuktuk.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateRequest {

    @NotNull(message = "Pickup latitude is required")
    @JsonProperty("pickup_lat")
    private BigDecimal pickupLat;

    @NotNull(message = "Pickup longitude is required")
    @JsonProperty("pickup_long")
    private BigDecimal pickupLong;

    @NotNull(message = "Drop latitude is required")
    @JsonProperty("drop_lat")
    private BigDecimal dropLat;

    @NotNull(message = "Drop longitude is required")
    @JsonProperty("drop_long")
    private BigDecimal dropLong;

    @NotNull(message = "Vehicle type is required")
    @JsonProperty("vehicle_type")
    private Long vehicleTypeId;

}
