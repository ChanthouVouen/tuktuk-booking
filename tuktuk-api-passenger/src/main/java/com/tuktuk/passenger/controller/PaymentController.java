package com.tuktuk.passenger.controller;

import com.tuktuk.common.dto.PaymentResponse;
import com.tuktuk.core.service.PaymentService;
import com.tuktuk.domain.entity.Passenger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings/{bookingId}/payment")
@RequiredArgsConstructor
@Tag(name = "Payments")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Generate a Bakong KHQR payment QR for a completed booking")
    public ResponseEntity<PaymentResponse> generate(@AuthenticationPrincipal Passenger passenger,
                                                     @PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.generate(passenger.getId(), bookingId));
    }

    @GetMapping
    @Operation(summary = "Check the current status of this booking's payment against Bakong")
    public ResponseEntity<PaymentResponse> getStatus(@AuthenticationPrincipal Passenger passenger,
                                                      @PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getStatusForPassenger(passenger.getId(), bookingId));
    }

    @GetMapping(value = "/qr", produces = MediaType.IMAGE_PNG_VALUE)
    @Operation(summary = "Get the KHQR payment code as a scannable PNG image")
    public ResponseEntity<byte[]> getQrImage(@AuthenticationPrincipal Passenger passenger,
                                              @PathVariable Long bookingId) {
        byte[] png = paymentService.getQrImageForPassenger(passenger.getId(), bookingId);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(png);
    }

}
