package com.lfss.libixel.shared.infrastructure.api;

public record ValidationError(
   String field,
   String message
) {}
