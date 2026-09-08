package com.tuktuk.passenger.controller;

import com.tuktuk.common.dto.AuthRequest;
import com.tuktuk.common.dto.AuthResponse;
import com.tuktuk.common.dto.PassengerRegisterRequest;
import com.tuktuk.core.service.PassengerAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/passengers/auth")
@RequiredArgsConstructor
@Tag(name = "Passenger Auth")
public class PassengerAuthController {

    private final PassengerAuthService passengerAuthService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody PassengerRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(passengerAuthService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(passengerAuthService.login(request));
    }

}
