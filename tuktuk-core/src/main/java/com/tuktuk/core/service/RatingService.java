package com.tuktuk.core.service;

import com.tuktuk.common.dto.RatingCreateRequest;
import com.tuktuk.common.dto.RatingResponse;
import com.tuktuk.common.exception.DuplicateResourceException;
import com.tuktuk.common.exception.InvalidStateException;
import com.tuktuk.common.exception.ResourceNotFoundException;
import com.tuktuk.core.mapper.RatingMapper;
import com.tuktuk.domain.entity.Booking;
import com.tuktuk.domain.entity.Notification;
import com.tuktuk.domain.entity.Rating;
import com.tuktuk.domain.enums.BookingStatus;
import com.tuktuk.domain.repository.BookingRepository;
import com.tuktuk.domain.repository.NotificationRepository;
import com.tuktuk.domain.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final BookingRepository bookingRepository;
    private final RatingRepository ratingRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public RatingResponse create(Long passengerId, Long bookingId, RatingCreateRequest request) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (!booking.getPassenger().getId().equals(passengerId)) {
            throw new InvalidStateException("Only the passenger who made the booking can rate this driver");
        }
        if (booking.getDriver() == null) {
            throw new InvalidStateException("This booking has no assigned driver");
        }
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new InvalidStateException("Booking must be completed before it can be rated");
        }
        if (ratingRepository.existsByBookingId(bookingId)) {
            throw new DuplicateResourceException("This booking has already been rated");
        }

        Rating rating = ratingRepository.save(Rating.builder()
                .booking(booking)
                .driver(booking.getDriver())
                .passenger(booking.getPassenger())
                .score(request.getScore())
                .comment(request.getComment())
                .build());

        notificationRepository.save(Notification.builder()
                .driver(booking.getDriver())
                .message("The passenger rated you " + request.getScore() + "/5 for booking " + bookingId)
                .build());

        return RatingMapper.toResponse(rating);
    }
}
