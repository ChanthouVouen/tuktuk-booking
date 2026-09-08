package com.tuktuk.core.mapper;

import com.tuktuk.common.dto.PassengerResponse;
import com.tuktuk.domain.entity.Passenger;

public final class PassengerMapper {

    private PassengerMapper() {
    }

    public static PassengerResponse toResponse(Passenger passenger) {
        return PassengerResponse.builder()
                .id(passenger.getId())
                .fullName(passenger.getFullName())
                .email(passenger.getEmail())
                .phoneNumber(passenger.getPhoneNumber())
                .createdAt(passenger.getCreatedAt())
                .updatedAt(passenger.getUpdatedAt())
                .build();
    }

}
