package com.tuktuk.driver.controller;

import com.tuktuk.core.booking.BookingService;
import com.tuktuk.core.booking.dto.BookingResponse;
import com.tuktuk.domain.driver.Driver;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/{bookingId}/accept")
    public ResponseEntity<BookingResponse> accept(@AuthenticationPrincipal Driver driver,
                                                   @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.accept(driver.getId(), bookingId));
    }

}