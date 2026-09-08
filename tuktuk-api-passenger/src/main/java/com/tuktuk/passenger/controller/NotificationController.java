package com.tuktuk.passenger.controller;

import com.tuktuk.core.notification.NotificationService;
import com.tuktuk.core.notification.dto.NotificationResponse;
import com.tuktuk.domain.passenger.Passenger;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications")
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("hasRole('PASSENGER')")
    public ResponseEntity<List<NotificationResponse>> findMine(@AuthenticationPrincipal Passenger passenger) {
        return ResponseEntity.ok(notificationService.findForPassenger(passenger.getId()));
    }
}