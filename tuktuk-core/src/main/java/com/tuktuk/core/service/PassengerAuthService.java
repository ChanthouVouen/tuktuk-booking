package com.tuktuk.core.service;

import com.tuktuk.common.dto.AuthRequest;
import com.tuktuk.common.dto.AuthResponse;
import com.tuktuk.common.dto.PassengerRegisterRequest;

public interface PassengerAuthService {

    AuthResponse register(PassengerRegisterRequest request);

    AuthResponse login(AuthRequest request);

}
