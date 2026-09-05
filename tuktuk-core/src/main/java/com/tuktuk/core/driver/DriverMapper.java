package com.tuktuk.core.driver;

import com.tuktuk.core.driver.dto.DriverResponse;
import com.tuktuk.domain.driver.Driver;

final class DriverMapper {

    private DriverMapper() {
    }

    static DriverResponse toResponse(Driver driver) {
        return DriverResponse.builder()
                .id(driver.getId())
                .fullName(driver.getFullName())
                .email(driver.getEmail())
                .phoneNumber(driver.getPhoneNumber())
                .licenseNumber(driver.getLicenseNumber())
                .vehiclePlate(driver.getVehiclePlate())
                .createdAt(driver.getCreatedAt())
                .updatedAt(driver.getUpdatedAt())
                .build();
    }

}
