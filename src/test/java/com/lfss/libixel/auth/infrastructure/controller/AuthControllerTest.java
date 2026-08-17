package com.lfss.libixel.auth.infrastructure.controller;

import com.lfss.libixel.auth.application.dto.RegisterUserResponse;
import com.lfss.libixel.auth.application.usecase.LoginUserUseCase;
import com.lfss.libixel.auth.application.usecase.RegisterUserUseCase;
import com.lfss.libixel.config.SecurityConfig;
import com.lfss.libixel.shared.exceptions.EmailAlreadyUsedException;
import com.lfss.libixel.shared.exceptions.InvalidCredentialException;
import com.lfss.libixel.shared.exceptions.UsernameAlreadyUsedException;
import com.lfss.libixel.shared.infrastructure.api.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@Import({GlobalExceptionHandler.class, SecurityConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;

    @MockitoBean
    private LoginUserUseCase loginUserUseCase;

    @Test
    void register_whenRequestIsValid_returnsCreated() throws Exception {
        UUID id = UUID.randomUUID();
        when(registerUserUseCase.execute(any()))
                .thenReturn(new RegisterUserResponse(id, "pixel_artist", "pixel@example.com"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "pixel_artist",
                                  "email": "pixel@example.com",
                                  "password": "secret12"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.username").value("pixel_artist"))
                .andExpect(jsonPath("$.email").value("pixel@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());

        verify(registerUserUseCase).execute(any());
    }

    @Test
    void register_whenRequestIsInvalid_returnsBadRequestProblem() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "ab",
                                  "email": "not-an-email",
                                  "password": "123"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("https://api.libixel.dev/problems/validation"))
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray());

        verify(registerUserUseCase, never()).execute(any());
    }

    @Test
    void register_whenUsernameTaken_returnsConflictProblem() throws Exception {
        when(registerUserUseCase.execute(any()))
                .thenThrow(new UsernameAlreadyUsedException("Username is already in use."));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "pixel_artist",
                                  "email": "pixel@example.com",
                                  "password": "secret12"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value("https://api.libixel.dev/problems/already-exists"))
                .andExpect(jsonPath("$.title").value("Already exists"))
                .andExpect(jsonPath("$.error.field").value("username"))
                .andExpect(jsonPath("$.error.message").value("Username is already in use."));
    }

    @Test
    void register_whenEmailTaken_returnsConflictProblem() throws Exception {
        when(registerUserUseCase.execute(any()))
                .thenThrow(new EmailAlreadyUsedException("Email is already in use."));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "pixel_artist",
                                  "email": "pixel@example.com",
                                  "password": "secret12"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value("https://api.libixel.dev/problems/already-exists"))
                .andExpect(jsonPath("$.title").value("Already exists"))
                .andExpect(jsonPath("$.error.field").value("email"))
                .andExpect(jsonPath("$.error.message").value("Email is already in use."));
    }

    @Test
    void login_whenCredentialsAreInvalid_returnsUnauthorizedProblemWithoutLeakingCause() throws Exception {
        when(loginUserUseCase.execute(any()))
                .thenThrow(new InvalidCredentialException());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "pixel_artist",
                                  "password": "wrongpass"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.type").value("https://api.libixel.dev/problems/invalid-credentials"))
                .andExpect(jsonPath("$.title").value("Invalid credentials"))
                .andExpect(jsonPath("$.detail").value("Username or password is incorrect."))
                .andExpect(jsonPath("$.error").doesNotExist());
    }
}
