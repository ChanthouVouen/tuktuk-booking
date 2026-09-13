package com.tuktuk.driver.controller;

import com.tuktuk.common.dto.VehicleCreateRequest;
import com.tuktuk.common.dto.VehicleResponse;
import com.tuktuk.core.service.VehicleService;
import com.tuktuk.domain.entity.Driver;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    @Operation(summary = "Create a new vehicle for the current driver")
    public ResponseEntity<VehicleResponse> create(@AuthenticationPrincipal Driver driver,
                                                   @Valid @RequestBody VehicleCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.create(driver, request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Get the current driver's vehicle")
    public ResponseEntity<VehicleResponse> findCurrent(@AuthenticationPrincipal Driver driver) {
        return ResponseEntity.ok(vehicleService.findCurrent(driver));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('DRIVER')")
    @Operation(summary = "Update the current driver's vehicle (e.g. to fix its type)")
    public ResponseEntity<VehicleResponse> update(@AuthenticationPrincipal Driver driver,
                                                   @Valid @RequestBody VehicleCreateRequest request) {
        return ResponseEntity.ok(vehicleService.update(driver, request));
    }
}
