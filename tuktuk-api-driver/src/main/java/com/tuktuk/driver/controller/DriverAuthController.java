package com.tuktuk.driver.controller;

import com.tuktuk.common.dto.AuthRequest;
import com.tuktuk.common.dto.AuthResponse;
import com.tuktuk.core.driver.DriverAuthService;
import com.tuktuk.core.driver.dto.DriverRegisterRequest;
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
@RequestMapping("/api/drivers/auth")
@RequiredArgsConstructor
@Tag(name = "Driver Auth")
public class DriverAuthController {

    private final DriverAuthService driverAuthService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody DriverRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(driverAuthService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(driverAuthService.login(request));
    }

}
