package com.tuktuk.core.booking;

import com.tuktuk.core.booking.dto.BookingResponse;
import com.tuktuk.domain.booking.Booking;
import com.tuktuk.domain.driver.Driver;
import com.tuktuk.domain.vehicletype.VehicleType;

final class BookingMapper {

    private BookingMapper() {
    }

    static BookingResponse toResponse(Booking booking) {
        Driver driver = booking.getDriver();
        VehicleType vehicleType = booking.getVehicleType();
        return BookingResponse.builder()
                .id(booking.getId())
                .passengerId(booking.getPassenger().getId())
                .driverId(driver != null ? driver.getId() : null)
                .pickupLat(booking.getPickupLat())
                .pickupLong(booking.getPickupLong())
                .dropLat(booking.getDropLat())
                .dropLong(booking.getDropLong())
                .distanceKm(booking.getDistanceKm())
                .price(booking.getPrice())
                .status(booking.getStatus())
                .vehicleTypeId(vehicleType.getId())
                .vehicleTypeName(vehicleType.getTypeName())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }

}
