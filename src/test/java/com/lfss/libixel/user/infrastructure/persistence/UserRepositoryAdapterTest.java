package com.lfss.libixel.user.infrastructure.persistence;

import com.lfss.libixel.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    private static final String USERNAME = "pixel_artist";
    private static final String EMAIL = "pixel@example.com";
    private static final String HASHED_PASSWORD = "{bcrypt}hashed";

    @Mock
    private SpringDataUserRepository springDataUserRepository;

    @InjectMocks
    private UserRepositoryAdapter adapter;

    @Test
    void existsByUsername_delegatesToSpringData() {
        when(springDataUserRepository.existsByUsername(USERNAME)).thenReturn(true);

        assertThat(adapter.existsByUsername(USERNAME)).isTrue();
        verify(springDataUserRepository).existsByUsername(USERNAME);
    }

    @Test
    void existsByEmail_delegatesToSpringData() {
        when(springDataUserRepository.existsByEmail(EMAIL)).thenReturn(false);

        assertThat(adapter.existsByEmail(EMAIL)).isFalse();
        verify(springDataUserRepository).existsByEmail(EMAIL);
    }

    @Test
    void save_delegatesToSpringData() {
        User user = User.register(USERNAME, EMAIL, HASHED_PASSWORD);
        when(springDataUserRepository.save(user)).thenReturn(user);

        User saved = adapter.save(user);

        assertThat(saved).isSameAs(user);
        verify(springDataUserRepository).save(user);
    }
}
