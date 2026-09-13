package com.tuktuk.core.mapper;

import com.tuktuk.common.dto.DriverResponse;
import com.tuktuk.domain.entity.Driver;

public final class DriverMapper {

    private DriverMapper() {
    }

    public static DriverResponse toResponse(Driver driver) {
        return DriverResponse.builder()
                .id(driver.getId())
                .fullName(driver.getFullName())
                .email(driver.getEmail())
                .phoneNumber(driver.getPhoneNumber())
                .licenseNumber(driver.getLicenseNumber())
                .vehiclePlate(driver.getVehiclePlate())
                .bakongAccountId(driver.getBakongAccountId())
                .merchantName(driver.getMerchantName())
                .createdAt(driver.getCreatedAt())
                .updatedAt(driver.getUpdatedAt())
                .build();
    }

}
