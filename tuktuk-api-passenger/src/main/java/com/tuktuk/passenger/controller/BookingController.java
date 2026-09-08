package com.tuktuk.passenger.controller;

import com.tuktuk.core.booking.BookingService;
import com.tuktuk.core.booking.dto.BookingCreateRequest;
import com.tuktuk.core.booking.dto.BookingResponse;
import com.tuktuk.core.rating.RatingService;
import com.tuktuk.core.rating.dto.RatingCreateRequest;
import com.tuktuk.core.rating.dto.RatingResponse;
import com.tuktuk.domain.passenger.Passenger;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings")
@SecurityRequirement(name = "bearerAuth")
public class BookingController {

    private final BookingService bookingService;
    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<BookingResponse> create(@AuthenticationPrincipal Passenger passenger,
                                                   @Valid @RequestBody BookingCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.create(passenger.getId(), request));
    }

    @PostMapping("/{bookingId}/rating")
    public ResponseEntity<RatingResponse> rateDriver(@AuthenticationPrincipal Passenger passenger,
                                                      @PathVariable Long bookingId,
                                                      @Valid @RequestBody RatingCreateRequest request) {
        return ResponseEntity.ok(ratingService.create(passenger.getId(), bookingId, request));
    }

}
