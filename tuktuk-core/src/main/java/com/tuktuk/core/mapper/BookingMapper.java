package com.tuktuk.core.mapper;

import com.tuktuk.common.dto.BookingResponse;
import com.tuktuk.domain.entity.Booking;
import com.tuktuk.domain.entity.Driver;
import com.tuktuk.domain.entity.VehicleType;

public final class BookingMapper {

    private BookingMapper() {
    }

    public static BookingResponse toResponse(Booking booking) {
        Driver driver = booking.getDriver();
        VehicleType vehicleType = booking.getVehicleType();
        return BookingResponse.builder()
                .id(booking.getId())
                .passenger(PassengerMapper.toResponse(booking.getPassenger()))
                .driver(driver != null ? DriverMapper.toResponse(driver) : null)
                .pickupLat(booking.getPickupLat())
                .pickupLong(booking.getPickupLong())
                .dropLat(booking.getDropLat())
                .dropLong(booking.getDropLong())
                .distanceKm(booking.getDistanceKm())
                .price(booking.getPrice())
                .status(booking.getStatus().name())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getTypeName())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }

}
