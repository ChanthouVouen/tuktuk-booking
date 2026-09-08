package com.tuktuk.core.service;

import com.tuktuk.common.dto.BookingCreateRequest;
import com.tuktuk.common.dto.BookingResponse;
import com.tuktuk.common.exception.InvalidStateException;
import com.tuktuk.common.exception.ResourceNotFoundException;
import com.tuktuk.core.mapper.BookingMapper;
import com.tuktuk.domain.entity.Booking;
import com.tuktuk.domain.entity.Driver;
import com.tuktuk.domain.entity.Notification;
import com.tuktuk.domain.entity.Passenger;
import com.tuktuk.domain.entity.VehicleType;
import com.tuktuk.domain.enums.BookingStatus;
import com.tuktuk.domain.repository.BookingRepository;
import com.tuktuk.domain.repository.DriverRepository;
import com.tuktuk.domain.repository.NotificationRepository;
import com.tuktuk.domain.repository.PassengerRepository;
import com.tuktuk.domain.repository.VehicleTypeRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
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
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public List<BookingResponse> findAll() {
        return bookingRepository.findAll().stream()
                .map(BookingMapper::toResponse)
                .toList();
    }

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

    @Transactional
    public BookingResponse complete(Long driverId, Long bookingId) {
        Booking booking = bookingRepository.findByIdForUpdate(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getDriver() == null || !booking.getDriver().getId().equals(driverId)) {
            throw new InvalidStateException("Only the assigned driver can complete this booking");
        }
        if (booking.getStatus() != BookingStatus.ACCEPTED && booking.getStatus() != BookingStatus.ONGOING) {
            throw new InvalidStateException("Only accepted or ongoing bookings can be completed");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        Booking completedBooking = bookingRepository.save(booking);
        notificationRepository.save(Notification.builder()
                .passenger(booking.getPassenger())
                .message("Your ride is complete. Please rate your driver for booking " + bookingId)
                .build());
        return BookingMapper.toResponse(completedBooking);
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
