package com.tuktuk.core.service;

import com.tuktuk.common.dto.PassengerResponse;
import com.tuktuk.common.dto.PassengerUpdateRequest;

public interface PassengerService {

    PassengerResponse findById(Long id);

    PassengerResponse update(Long id, PassengerUpdateRequest request);

}
