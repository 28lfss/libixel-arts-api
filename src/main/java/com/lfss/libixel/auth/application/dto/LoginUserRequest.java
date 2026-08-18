package com.lfss.libixel.auth.application.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginUserRequest(

        @NotBlank(message = "{username.notblank}")
        String username,

        @NotBlank(message = "{password.notblank}")
        String password
) {
}
