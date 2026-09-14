package com.tuktuk.driver.controller;

import com.tuktuk.common.dto.DriverResponse;
import com.tuktuk.common.dto.DriverUpdateRequest;
import com.tuktuk.core.service.DriverService;
import com.tuktuk.domain.entity.Driver;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@Tag(name = "Drivers")
@SecurityRequirement(name = "bearerAuth")
public class DriverController {

    private final DriverService driverService;

    @GetMapping("/me")
    @Operation(summary = "Get the current driver's information")
    public ResponseEntity<DriverResponse> findCurrent(@AuthenticationPrincipal Driver driver) {
        return ResponseEntity.ok(driverService.findById(driver.getId()));
    }

    @PutMapping("/me")
    @Operation(summary = "Update the current driver's information")
    public ResponseEntity<DriverResponse> update(@AuthenticationPrincipal Driver driver,
                                                  @Valid @RequestBody DriverUpdateRequest request) {
        return ResponseEntity.ok(driverService.update(driver.getId(), request));
    }

}
