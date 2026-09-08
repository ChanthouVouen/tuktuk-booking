package com.tuktuk.driver.controller;

import com.tuktuk.core.vehicle.VehicleService;
import com.tuktuk.core.vehicle.dto.VehicleCreateRequest;
import com.tuktuk.core.vehicle.dto.VehicleResponse;
import com.tuktuk.domain.driver.Driver;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicles")
@SecurityRequirement(name = "bearerAuth")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<VehicleResponse> create(@AuthenticationPrincipal Driver driver,
                                                   @Valid @RequestBody VehicleCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.create(driver, request));
    }
}