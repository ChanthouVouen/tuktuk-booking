package com.tuktuk.core.service;

import com.tuktuk.common.dto.BookingCreateRequest;
import com.tuktuk.common.dto.BookingResponse;
import java.util.List;

public interface BookingService {

    /** Bookings not yet accepted by any driver, available for a driver to accept. */
    List<BookingResponse> findAllPending();

    List<BookingResponse> findAll(Long passengerId);

    /** A driver's own booking history (accepted, ongoing, completed, etc). */
    List<BookingResponse> findAllForDriver(Long driverId);

    BookingResponse findById(Long id, Long passengerId);

    BookingResponse create(Long passengerId, BookingCreateRequest request);

    BookingResponse accept(Long driverId, Long bookingId);

    BookingResponse complete(Long driverId, Long bookingId);

    BookingResponse cancel(Long id, Long passengerId);

}
