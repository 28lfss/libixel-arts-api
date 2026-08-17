package com.lfss.libixel.auth.application.security;

public interface PasswordHasher {
    String hash(String rawPassword);

    boolean match(String rawPassword, String hashedPassword);
}
