package com.lfss.libixel.auth.application.dto;

import java.util.UUID;

public record LoginUserResponse(
        UUID id,
        String username
) {
}
