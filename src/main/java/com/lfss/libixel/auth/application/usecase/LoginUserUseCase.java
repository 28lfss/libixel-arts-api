package com.lfss.libixel.auth.application.usecase;

import com.lfss.libixel.auth.application.dto.LoginUserRequest;
import com.lfss.libixel.auth.application.dto.LoginUserResponse;

public interface LoginUserUseCase {
    LoginUserResponse execute(LoginUserRequest request);
}
