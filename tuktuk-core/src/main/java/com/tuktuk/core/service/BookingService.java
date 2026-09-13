package com.tuktuk.core.service;

import com.tuktuk.common.dto.BookingCreateRequest;
import com.tuktuk.common.dto.BookingResponse;
import java.util.List;

public interface BookingService {

    List<BookingResponse> findAll();

    List<BookingResponse> findAll(Long passengerId);

    BookingResponse findById(Long id, Long passengerId);

    BookingResponse create(Long passengerId, BookingCreateRequest request);

    BookingResponse accept(Long driverId, Long bookingId);

    BookingResponse complete(Long driverId, Long bookingId);

    BookingResponse cancel(Long id, Long passengerId);

}
