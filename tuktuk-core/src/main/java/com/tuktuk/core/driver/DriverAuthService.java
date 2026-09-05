package com.tuktuk.core.driver;

import com.tuktuk.common.dto.AuthRequest;
import com.tuktuk.common.dto.AuthResponse;
import com.tuktuk.common.exception.DuplicateResourceException;
import com.tuktuk.common.exception.ResourceNotFoundException;
import com.tuktuk.common.security.JwtService;
import com.tuktuk.core.driver.dto.DriverRegisterRequest;
import com.tuktuk.domain.driver.Driver;
import com.tuktuk.domain.driver.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DriverAuthService {

    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(DriverRegisterRequest request) {
        if (driverRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("A driver with this email already exists");
        }

        Driver driver = Driver.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .licenseNumber(request.getLicenseNumber())
                .vehiclePlate(request.getVehiclePlate())
                .build();
        driverRepository.save(driver);

        return AuthResponse.builder().token(jwtService.generateToken(driver)).build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Driver driver = driverRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with email: " + request.getEmail()));

        return AuthResponse.builder().token(jwtService.generateToken(driver)).build();
    }

}
