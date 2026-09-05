package com.tuktuk.core.passenger;

import com.tuktuk.core.passenger.dto.PassengerResponse;
import com.tuktuk.domain.passenger.Passenger;

final class PassengerMapper {

    private PassengerMapper() {
    }

    static PassengerResponse toResponse(Passenger passenger) {
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
