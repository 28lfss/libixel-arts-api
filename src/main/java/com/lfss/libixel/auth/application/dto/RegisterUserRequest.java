package com.lfss.libixel.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest (

    @NotBlank(message = "{username.notblank}")
    @Size(min = 4, max = 18, message = "{username.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "{username.invalid}")
    String username,

    @NotBlank(message = "{email.notblank}")
    @Email(message = "{email.invalid}")
    String email,

    @NotBlank(message = "{password.notblank}")
    @Size(min = 6, max = 32, message = "{password.size}")
    String password
) {}
