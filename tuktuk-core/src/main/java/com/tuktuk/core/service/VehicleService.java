package com.tuktuk.core.service;

import com.tuktuk.common.dto.VehicleCreateRequest;
import com.tuktuk.common.dto.VehicleResponse;
import com.tuktuk.common.exception.DuplicateResourceException;
import com.tuktuk.core.mapper.VehicleMapper;
import com.tuktuk.domain.entity.Driver;
import com.tuktuk.domain.entity.Vehicle;
import com.tuktuk.domain.repository.VehicleRepository;
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

        return VehicleMapper.toResponse(vehicleRepository.save(vehicle));
    }
}
