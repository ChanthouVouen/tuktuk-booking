package com.tuktuk.passenger.controller;

import com.tuktuk.domain.vehicletype.VehicleType;
import com.tuktuk.domain.vehicletype.VehicleTypeRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicle-types")
@RequiredArgsConstructor
@Tag(name = "Vehicle Types")
public class VehicleTypeController {

    private final VehicleTypeRepository vehicleTypeRepository;

    @GetMapping
    public ResponseEntity<List<VehicleType>> findAll() {
        return ResponseEntity.ok(vehicleTypeRepository.findAll());
    }
}