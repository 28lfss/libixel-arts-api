package com.lfss.libixel.auth.infrastructure.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordEncoderHasherTest {

    private static final String PASSWORD = "secret12";
    private static final String HASHED_PASSWORD = "{bcrypt}hashed";

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordEncoderHasher hasher;

    @Test
    void hash_delegatesToPasswordEncoder() {
        when(passwordEncoder.encode(PASSWORD)).thenReturn(HASHED_PASSWORD);

        String result = hasher.hash(PASSWORD);

        assertThat(result).isEqualTo(HASHED_PASSWORD);
        verify(passwordEncoder).encode(PASSWORD);
    }
}
