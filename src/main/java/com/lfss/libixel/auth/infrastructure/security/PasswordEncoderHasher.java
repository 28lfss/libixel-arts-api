package com.lfss.libixel.auth.infrastructure.security;

import com.lfss.libixel.auth.application.security.PasswordHasher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderHasher implements PasswordHasher {
    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderHasher(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String hash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
