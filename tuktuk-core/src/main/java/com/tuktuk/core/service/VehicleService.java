package com.tuktuk.core.service;

import com.tuktuk.common.dto.VehicleCreateRequest;
import com.tuktuk.common.dto.VehicleResponse;
import com.tuktuk.domain.entity.Driver;

public interface VehicleService {

    VehicleResponse create(Driver driver, VehicleCreateRequest request);

    VehicleResponse findCurrent(Driver driver);

    VehicleResponse update(Driver driver, VehicleCreateRequest request);

}
