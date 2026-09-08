package com.tuktuk.core.rating;

import com.tuktuk.common.exception.DuplicateResourceException;
import com.tuktuk.common.exception.InvalidStateException;
import com.tuktuk.common.exception.ResourceNotFoundException;
import com.tuktuk.core.rating.dto.RatingCreateRequest;
import com.tuktuk.core.rating.dto.RatingResponse;
import com.tuktuk.domain.booking.Booking;
import com.tuktuk.domain.booking.BookingRepository;
import com.tuktuk.domain.booking.BookingStatus;
import com.tuktuk.domain.notification.Notification;
import com.tuktuk.domain.notification.NotificationRepository;
import com.tuktuk.domain.rating.Rating;
import com.tuktuk.domain.rating.RatingRepository;
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

        return toResponse(rating);
    }

    private RatingResponse toResponse(Rating rating) {
        return RatingResponse.builder()
                .id(rating.getId())
                .bookingId(rating.getBooking().getId())
                .driverId(rating.getDriver().getId())
                .passengerId(rating.getPassenger().getId())
                .score(rating.getScore())
                .comment(rating.getComment())
                .createdAt(rating.getCreatedAt())
                .build();
    }
}