package com.tuktuk.core.service;

import com.tuktuk.common.dto.AuthRequest;
import com.tuktuk.common.dto.AuthResponse;
import com.tuktuk.common.dto.DriverRegisterRequest;

public interface DriverAuthService {

    AuthResponse register(DriverRegisterRequest request);

    AuthResponse login(AuthRequest request);

}
