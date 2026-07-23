package com.lfss.libixel.auth.application.service;

import com.lfss.libixel.auth.application.dto.RegisterUserRequest;
import com.lfss.libixel.auth.application.dto.RegisterUserResponse;
import com.lfss.libixel.auth.application.mapper.RegisterMapper;
import com.lfss.libixel.auth.application.security.PasswordHasher;
import com.lfss.libixel.shared.exceptions.EmailAlreadyUsedException;
import com.lfss.libixel.shared.exceptions.UsernameAlreadyUsedException;
import com.lfss.libixel.user.domain.User;
import com.lfss.libixel.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {

    private static final RegisterUserRequest REQUEST =
            new RegisterUserRequest("pixel_artist", "pixel@example.com", "secret12");

    private static final String HASHED_PASSWORD = "{bcrypt}hashed";
    private static final String USERNAME_ALREADY_USED = "Username is already in use.";
    private static final String EMAIL_ALREADY_USED = "Email is already in use.";

    @Mock
    private UserRepository repository;

    @Mock
    private RegisterMapper mapper;

    @Mock
    private PasswordHasher hasher;

    @InjectMocks
    private RegisterUserService service;

    @Test
    void execute_whenCredentialsAreFree_registersHashedUser() {
        UUID id = UUID.randomUUID();
        RegisterUserResponse expected = new RegisterUserResponse(id, REQUEST.username(), REQUEST.email());

        when(repository.existsByUsername(REQUEST.username())).thenReturn(false);
        when(repository.existsByEmail(REQUEST.email())).thenReturn(false);
        when(hasher.hash(REQUEST.password())).thenReturn(HASHED_PASSWORD);
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toResponse(any(User.class))).thenReturn(expected);

        RegisterUserResponse result = service.execute(REQUEST);

        assertThat(result).isEqualTo(expected);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(hasher).hash(REQUEST.password());
        verify(repository).save(userCaptor.capture());
        verify(mapper).toResponse(userCaptor.getValue());

        User saved = userCaptor.getValue();
        assertThat(saved.getUsername()).isEqualTo(REQUEST.username());
        assertThat(saved.getEmail()).isEqualTo(REQUEST.email());
        assertThat(saved.getPasswordHash()).isEqualTo(HASHED_PASSWORD);
    }

    @Test
    void execute_whenUsernameExists_throwsAndDoesNotPersist() {
        when(repository.existsByUsername(REQUEST.username())).thenReturn(true);

        assertThatThrownBy(() -> service.execute(REQUEST))
                .isInstanceOf(UsernameAlreadyUsedException.class)
                .hasMessage(USERNAME_ALREADY_USED);

        verify(repository, never()).existsByEmail(any());
        verify(repository, never()).save(any());
        verifyNoInteractions(hasher, mapper);
    }

    @Test
    void execute_whenEmailExists_throwsAndDoesNotPersist() {
        when(repository.existsByUsername(REQUEST.username())).thenReturn(false);
        when(repository.existsByEmail(REQUEST.email())).thenReturn(true);

        assertThatThrownBy(() -> service.execute(REQUEST))
                .isInstanceOf(EmailAlreadyUsedException.class)
                .hasMessage(EMAIL_ALREADY_USED);

        verify(repository, never()).save(any());
        verifyNoInteractions(hasher, mapper);
    }
}
