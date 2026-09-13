package com.tuktuk.core.service;

import com.tuktuk.common.dto.DriverResponse;
import com.tuktuk.common.dto.DriverUpdateRequest;

public interface DriverService {

    DriverResponse findById(Long id);

    DriverResponse update(Long id, DriverUpdateRequest request);

}
