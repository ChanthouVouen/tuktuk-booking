package com.tuktuk.driver.controller;

import com.tuktuk.common.dto.NotificationResponse;
import com.tuktuk.core.service.NotificationService;
import com.tuktuk.domain.entity.Driver;
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
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<List<NotificationResponse>> findMine(@AuthenticationPrincipal Driver driver) {
        return ResponseEntity.ok(notificationService.findForDriver(driver.getId()));
    }
}