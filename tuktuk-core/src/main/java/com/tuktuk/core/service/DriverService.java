package com.tuktuk.core.service;

import com.tuktuk.common.dto.DriverResponse;
import com.tuktuk.common.dto.DriverUpdateRequest;
import com.tuktuk.common.exception.ResourceNotFoundException;
import com.tuktuk.core.mapper.DriverMapper;
import com.tuktuk.domain.entity.Driver;
import com.tuktuk.domain.repository.DriverRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    @Transactional(readOnly = true)
    public List<DriverResponse> findAll() {
        return driverRepository.findAll().stream()
                .map(DriverMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DriverResponse findById(Long id) {
        return DriverMapper.toResponse(getDriverOrThrow(id));
    }

    @Transactional
    public DriverResponse update(Long id, DriverUpdateRequest request) {
        Driver driver = getDriverOrThrow(id);
        driver.setFullName(request.getFullName());
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setVehiclePlate(request.getVehiclePlate());
        return DriverMapper.toResponse(driverRepository.save(driver));
    }

    @Transactional
    public void delete(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new ResourceNotFoundException("Driver not found with id: " + id);
        }
        driverRepository.deleteById(id);
    }

    private Driver getDriverOrThrow(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));
    }

}
