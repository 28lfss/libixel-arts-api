package com.lfss.libixel.auth.infrastructure.controller;

import com.lfss.libixel.auth.application.dto.LoginUserRequest;
import com.lfss.libixel.auth.application.dto.LoginUserResponse;
import com.lfss.libixel.auth.application.usecase.LoginUserUseCase;
import com.lfss.libixel.auth.application.usecase.RegisterUserUseCase;
import com.lfss.libixel.auth.application.dto.RegisterUserRequest;
import com.lfss.libixel.auth.application.dto.RegisterUserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final RegisterUserUseCase registerUseCase;
    private final LoginUserUseCase loginUseCase;

    public AuthController(RegisterUserUseCase registerUseCase, LoginUserUseCase loginUseCase) {
        this.registerUseCase = registerUseCase;
        this.loginUseCase = loginUseCase;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public RegisterUserResponse registerUser(@Valid @RequestBody RegisterUserRequest request) {
        return registerUseCase.execute(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public LoginUserResponse loginUser(@Valid @RequestBody LoginUserRequest request) {
        return loginUseCase.execute(request);
    }

}
