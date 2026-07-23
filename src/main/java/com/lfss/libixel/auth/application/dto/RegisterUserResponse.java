package com.lfss.libixel.auth.application.dto;

import java.util.UUID;

public record RegisterUserResponse(UUID id, String username, String email) {}
