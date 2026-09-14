package com.tuktuk.core.service.serviceimpl;

import com.tuktuk.common.dto.VehicleCreateRequest;
import com.tuktuk.common.dto.VehicleResponse;
import com.tuktuk.common.exception.DuplicateResourceException;
import com.tuktuk.common.exception.ResourceNotFoundException;
import com.tuktuk.core.mapper.VehicleMapper;
import com.tuktuk.core.service.VehicleService;
import com.tuktuk.domain.entity.Driver;
import com.tuktuk.domain.entity.Vehicle;
import com.tuktuk.domain.entity.VehicleType;
import com.tuktuk.domain.repository.VehicleRepository;
import com.tuktuk.domain.repository.VehicleTypeRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleTypeRepository vehicleTypeRepository;

    @Override
    @Transactional
    public VehicleResponse create(Driver driver, VehicleCreateRequest request) {
        if (vehicleRepository.findByDriverId(driver.getId()).isPresent()) {
            throw new DuplicateResourceException("This driver already has a vehicle");
        }

        Vehicle vehicle = Vehicle.builder()
                .name(request.getName())
                .type(resolveTypeName(request.getType()))
                .driver(driver)
                .build();

        return VehicleMapper.toResponse(vehicleRepository.save(vehicle));
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleResponse findCurrent(Driver driver) {
        return VehicleMapper.toResponse(getVehicleOrThrow(driver));
    }

    @Override
    @Transactional
    public VehicleResponse update(Driver driver, VehicleCreateRequest request) {
        Vehicle vehicle = getVehicleOrThrow(driver);
        vehicle.setName(request.getName());
        vehicle.setType(resolveTypeName(request.getType()));
        return VehicleMapper.toResponse(vehicleRepository.save(vehicle));
    }

    /**
     * Rejects a vehicle type that isn't one of the registered {@link VehicleType}s, and
     * normalizes to that catalog's exact spelling/casing — so a vehicle's type can never
     * silently drift out of sync with what passengers pick from at booking time (which is
     * exactly what caused "Driver vehicle type does not match" to be so confusing: nothing
     * previously stopped a typo'd or extra-whitespace type string from being saved).
     */
    private String resolveTypeName(String requestedType) {
        return vehicleTypeRepository.findByTypeNameIgnoreCase(requestedType == null ? "" : requestedType.trim())
                .map(VehicleType::getTypeName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Unknown vehicle type '" + requestedType + "'. Valid types: "
                                + vehicleTypeRepository.findAll().stream()
                                        .map(VehicleType::getTypeName)
                                        .collect(Collectors.joining(", "))));
    }

    private Vehicle getVehicleOrThrow(Driver driver) {
        return vehicleRepository.findByDriverId(driver.getId())
                .orElseThrow(() -> new ResourceNotFoundException("You have not registered a vehicle yet"));
    }
}
