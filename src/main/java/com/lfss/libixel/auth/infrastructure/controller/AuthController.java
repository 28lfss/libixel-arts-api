package com.lfss.libixel.auth.infrastructure.controller;

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

    public AuthController(RegisterUserUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public RegisterUserResponse registerUser(@Valid @RequestBody RegisterUserRequest request) {
        return registerUseCase.execute(request);
    }

}
