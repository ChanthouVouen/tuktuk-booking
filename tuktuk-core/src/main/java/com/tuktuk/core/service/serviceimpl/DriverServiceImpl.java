package com.tuktuk.core.service.serviceimpl;

import com.tuktuk.common.dto.DriverResponse;
import com.tuktuk.common.dto.DriverUpdateRequest;
import com.tuktuk.common.exception.ResourceNotFoundException;
import com.tuktuk.core.mapper.DriverMapper;
import com.tuktuk.core.service.DriverService;
import com.tuktuk.domain.entity.Driver;
import com.tuktuk.domain.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    @Override
    @Transactional(readOnly = true)
    public DriverResponse findById(Long id) {
        return DriverMapper.toResponse(getDriverOrThrow(id));
    }

    @Override
    @Transactional
    public DriverResponse update(Long id, DriverUpdateRequest request) {
        Driver driver = getDriverOrThrow(id);
        driver.setFullName(request.getFullName());
        driver.setPhoneNumber(request.getPhoneNumber());
        driver.setVehiclePlate(request.getVehiclePlate());
        driver.setBakongAccountId(request.getBakongAccountId());
        driver.setMerchantName(request.getMerchantName());
        return DriverMapper.toResponse(driverRepository.save(driver));
    }

    private Driver getDriverOrThrow(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + id));
    }

}
