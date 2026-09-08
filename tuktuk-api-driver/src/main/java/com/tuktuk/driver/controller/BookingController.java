package com.tuktuk.driver.controller;

import com.tuktuk.core.booking.BookingService;
import com.tuktuk.core.booking.dto.BookingResponse;
import com.tuktuk.domain.driver.Driver;
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

    @GetMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<BookingResponse>> findAll() {
        return ResponseEntity.ok(bookingService.findAll());
    }

    @PostMapping("/{bookingId}/accept")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<BookingResponse> accept(@AuthenticationPrincipal Driver driver,
                                                   @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.accept(driver.getId(), bookingId));
    }

    @PostMapping("/{bookingId}/complete")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<BookingResponse> complete(@AuthenticationPrincipal Driver driver,
                                                     @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.complete(driver.getId(), bookingId));
    }

}