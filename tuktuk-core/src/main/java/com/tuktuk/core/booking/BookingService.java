package com.tuktuk.core.booking;

import com.tuktuk.common.exception.ResourceNotFoundException;
import com.tuktuk.common.exception.InvalidStateException;
import com.tuktuk.core.booking.dto.BookingCreateRequest;
import com.tuktuk.core.booking.dto.BookingResponse;
import com.tuktuk.domain.booking.Booking;
import com.tuktuk.domain.booking.BookingRepository;
import com.tuktuk.domain.booking.BookingStatus;
import com.tuktuk.domain.driver.Driver;
import com.tuktuk.domain.driver.DriverRepository;
import com.tuktuk.domain.passenger.Passenger;
import com.tuktuk.domain.passenger.PassengerRepository;
import com.tuktuk.domain.vehicletype.VehicleType;
import com.tuktuk.domain.vehicletype.VehicleTypeRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private final BookingRepository bookingRepository;
        private final DriverRepository driverRepository;
    private final PassengerRepository passengerRepository;
    private final VehicleTypeRepository vehicleTypeRepository;

    @Transactional
    public BookingResponse create(Long passengerId, BookingCreateRequest request) {
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with id: " + passengerId));

        VehicleType vehicleType = vehicleTypeRepository.findById(request.getVehicleTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle type not found with id: " + request.getVehicleTypeId()));

        BigDecimal distanceKm = calculateDistanceKm(request);

        Booking booking = Booking.builder()
                .passenger(passenger)
                .pickupLat(request.getPickupLat())
                .pickupLong(request.getPickupLong())
                .dropLat(request.getDropLat())
                .dropLong(request.getDropLong())
                .distanceKm(distanceKm)
                .price(distanceKm.multiply(vehicleType.getPrice()).setScale(2, RoundingMode.HALF_UP))
                .status(BookingStatus.PENDING)
                .vehicleType(vehicleType)
                .build();

        return BookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Transactional
    public BookingResponse accept(Long driverId, Long bookingId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + driverId));
        Booking booking = bookingRepository.findByIdForUpdate(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new InvalidStateException("Booking can only be accepted while it is pending");
        }

        booking.setDriver(driver);
        booking.setStatus(BookingStatus.ACCEPTED);
        return BookingMapper.toResponse(bookingRepository.save(booking));
    }

    private BigDecimal calculateDistanceKm(BookingCreateRequest request) {
        double distanceKm = haversineDistanceKm(
                request.getPickupLat().doubleValue(), request.getPickupLong().doubleValue(),
                request.getDropLat().doubleValue(), request.getDropLong().doubleValue());

        return BigDecimal.valueOf(distanceKm).setScale(3, RoundingMode.HALF_UP);
    }

    // Great-circle distance between the two coordinate pairs, in kilometers.
    private double haversineDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

}
