package com.tuktuk.core.vehicle;

import com.tuktuk.common.exception.DuplicateResourceException;
import com.tuktuk.core.vehicle.dto.VehicleCreateRequest;
import com.tuktuk.core.vehicle.dto.VehicleResponse;
import com.tuktuk.domain.driver.Driver;
import com.tuktuk.domain.vehicle.Vehicle;
import com.tuktuk.domain.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Transactional
    public VehicleResponse create(Driver driver, VehicleCreateRequest request) {
        if (vehicleRepository.findByDriverId(driver.getId()).isPresent()) {
            throw new DuplicateResourceException("This driver already has a vehicle");
        }

        Vehicle vehicle = Vehicle.builder()
                .name(request.getName())
                .type(request.getType())
                .driver(driver)
                .build();

        return toResponse(vehicleRepository.save(vehicle));
    }

    private VehicleResponse toResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .name(vehicle.getName())
                .type(vehicle.getType())
                .driverId(vehicle.getDriver().getId())
                .build();
    }
}