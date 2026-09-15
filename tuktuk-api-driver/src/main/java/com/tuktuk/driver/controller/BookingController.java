package com.tuktuk.driver.controller;

import com.tuktuk.common.dto.BookingResponse;
import com.tuktuk.core.service.BookingService;
import com.tuktuk.domain.entity.Driver;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings")
@SecurityRequirement(name = "bearerAuth")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/pending")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Get pending bookings available to accept")
    public ResponseEntity<List<BookingResponse>> findAllPending() {
        return ResponseEntity.ok(bookingService.findAllPending());
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Get the current driver's booking history (accepted, ongoing, completed, etc)")
    public ResponseEntity<List<BookingResponse>> findAllForDriver(@AuthenticationPrincipal Driver driver) {
        return ResponseEntity.ok(bookingService.findAllForDriver(driver.getId()));
    }

    @PostMapping("/{bookingId}/accept")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Accept a booking")
    public ResponseEntity<BookingResponse> accept(@AuthenticationPrincipal Driver driver,
                                                   @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.accept(driver.getId(), bookingId));
    }

    @PostMapping("/{bookingId}/complete")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Complete a booking")
    public ResponseEntity<BookingResponse> complete(@AuthenticationPrincipal Driver driver,
                                                     @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.complete(driver.getId(), bookingId));
    }

}