package com.tuktuk.passenger.controller;

import com.tuktuk.core.booking.BookingService;
import com.tuktuk.core.booking.dto.BookingCreateRequest;
import com.tuktuk.core.booking.dto.BookingResponse;
import com.tuktuk.core.rating.RatingService;
import com.tuktuk.core.rating.dto.RatingCreateRequest;
import com.tuktuk.core.rating.dto.RatingResponse;
import com.tuktuk.core.passenger.dto.PassengerResponse;
import com.tuktuk.core.passenger.dto.PassengerUpdateRequest;
import com.tuktuk.domain.passenger.Passenger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 2. Get booking history
    @GetMapping
    @Operation(summary = "Get booking history (All)")
    public ResponseEntity<List<BookingResponse>> findAll(@AuthenticationPrincipal Passenger passenger) {
        return ResponseEntity.ok(bookingService.findAll(passenger.getId()));
    }

    // 3. Get a booking by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get a booking by ID")
    public ResponseEntity<BookingResponse> findById( @AuthenticationPrincipal Passenger passenger, @PathVariable Long id) {
        return ResponseEntity.ok( bookingService.findById(id, passenger.getId()) ); }


    // 4. Cancel a booking
    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel a booking")
    public ResponseEntity<BookingResponse> cancel( @AuthenticationPrincipal Passenger passenger, @PathVariable Long id) {
        return ResponseEntity.ok( bookingService.cancel(id, passenger.getId()) ); }

    @PostMapping("/{bookingId}/rating")
    public ResponseEntity<RatingResponse> rateDriver(@AuthenticationPrincipal Passenger passenger,
                                                      @PathVariable Long bookingId,
                                                      @Valid @RequestBody RatingCreateRequest request) {
        return ResponseEntity.ok(ratingService.create(passenger.getId(), bookingId, request));
    }
}
