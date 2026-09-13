package com.tuktuk.driver.controller;

import com.tuktuk.common.dto.PaymentResponse;
import com.tuktuk.core.service.PaymentService;
import com.tuktuk.domain.entity.Driver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings/{bookingId}/payment")
@RequiredArgsConstructor
@Tag(name = "Payments")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Check whether the passenger has paid for this booking")
    public ResponseEntity<PaymentResponse> getStatus(@AuthenticationPrincipal Driver driver,
                                                      @PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getStatusForDriver(driver.getId(), bookingId));
    }

    @GetMapping(value = "/qr", produces = MediaType.IMAGE_PNG_VALUE)
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Get the KHQR payment code as a scannable PNG image")
    public ResponseEntity<byte[]> getQrImage(@AuthenticationPrincipal Driver driver,
                                              @PathVariable Long bookingId) {
        byte[] png = paymentService.getQrImageForDriver(driver.getId(), bookingId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(png);
    }

}
