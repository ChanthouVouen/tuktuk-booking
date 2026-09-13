package com.tuktuk.passenger.controller;

import com.tuktuk.common.dto.PassengerResponse;
import com.tuktuk.common.dto.PassengerUpdateRequest;
import com.tuktuk.core.service.PassengerService;
import com.tuktuk.domain.entity.Passenger;

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
@RequestMapping("/api/passengers")
@RequiredArgsConstructor
@Tag(name = "Passengers")
@SecurityRequirement(name = "bearerAuth")
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping("/me")
    @Operation(summary = "Get the current authenticated passenger's profile")
    public ResponseEntity<PassengerResponse> findCurrent(@AuthenticationPrincipal Passenger passenger) {
        return ResponseEntity.ok(passengerService.findById(passenger.getId()));
    }

    @PutMapping("/me")
    @Operation(summary = "Update the current authenticated passenger's profile")
    public ResponseEntity<PassengerResponse> update(@AuthenticationPrincipal Passenger passenger,
                                                      @Valid @RequestBody PassengerUpdateRequest request) {
        return ResponseEntity.ok(passengerService.update(passenger.getId(), request));
    }

}
