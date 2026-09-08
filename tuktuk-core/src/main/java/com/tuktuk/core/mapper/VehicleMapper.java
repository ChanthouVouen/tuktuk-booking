package com.tuktuk.core.mapper;

import com.tuktuk.common.dto.VehicleResponse;
import com.tuktuk.domain.entity.Vehicle;

public final class VehicleMapper {

    private VehicleMapper() {
    }

    public static VehicleResponse toResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .name(vehicle.getName())
                .type(vehicle.getType())
                .driverId(vehicle.getDriver().getId())
                .build();
    }

}
