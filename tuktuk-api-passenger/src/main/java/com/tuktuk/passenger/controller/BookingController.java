package com.tuktuk.passenger.controller;

import com.tuktuk.common.dto.BookingCreateRequest;
import com.tuktuk.common.dto.BookingResponse;
import com.tuktuk.common.dto.RatingCreateRequest;
import com.tuktuk.common.dto.RatingResponse;
import com.tuktuk.core.service.BookingService;
import com.tuktuk.core.service.RatingService;
import com.tuktuk.domain.entity.Passenger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    @Operation (summary = "Create a new booking for the current passenger")
    public ResponseEntity<BookingResponse> create(@AuthenticationPrincipal Passenger passenger,
                                                   @Valid @RequestBody BookingCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.create(passenger.getId(), request));
    }

    @GetMapping
    @Operation(summary = "Get the current passenger's booking history")
    public ResponseEntity<List<BookingResponse>> findAll(@AuthenticationPrincipal Passenger passenger) {
        return ResponseEntity.ok(bookingService.findAll(passenger.getId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one of the current passenger's bookings by id")
    public ResponseEntity<BookingResponse> findById(@AuthenticationPrincipal Passenger passenger, @PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findById(id, passenger.getId()));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel one of the current passenger's bookings")
    public ResponseEntity<BookingResponse> cancel(@AuthenticationPrincipal Passenger passenger, @PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancel(id, passenger.getId()));
    }

    @PostMapping("/{bookingId}/rating")
    @Operation(summary = "Rate the driver for a completed booking")
    public ResponseEntity<RatingResponse> rateDriver(@AuthenticationPrincipal Passenger passenger,
                                                      @PathVariable Long bookingId,
                                                      @Valid @RequestBody RatingCreateRequest request) {
        return ResponseEntity.ok(ratingService.create(passenger.getId(), bookingId, request));
    }

}
