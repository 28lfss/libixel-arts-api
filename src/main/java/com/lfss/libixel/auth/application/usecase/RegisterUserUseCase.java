package com.lfss.libixel.auth.application.usecase;

import com.lfss.libixel.auth.application.dto.RegisterUserRequest;
import com.lfss.libixel.auth.application.dto.RegisterUserResponse;

public interface RegisterUserUseCase {
    RegisterUserResponse execute(RegisterUserRequest request);
}
