package com.tuktuk.core.service;

import com.tuktuk.common.dto.AuthRequest;
import com.tuktuk.common.dto.AuthResponse;
import com.tuktuk.common.dto.PassengerRegisterRequest;
import com.tuktuk.common.exception.DuplicateResourceException;
import com.tuktuk.common.exception.ResourceNotFoundException;
import com.tuktuk.common.security.JwtService;
import com.tuktuk.domain.entity.Passenger;
import com.tuktuk.domain.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PassengerAuthService {

    private final PassengerRepository passengerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(PassengerRegisterRequest request) {
        if (passengerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("A passenger with this email already exists");
        }

        Passenger passenger = Passenger.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .build();
        passengerRepository.save(passenger);

        return AuthResponse.builder().token(jwtService.generateToken(passenger)).build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Passenger passenger = passengerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with email: " + request.getEmail()));

        return AuthResponse.builder().token(jwtService.generateToken(passenger)).build();
    }

}
