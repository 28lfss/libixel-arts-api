package com.lfss.libixel.auth.application.service;

import com.lfss.libixel.auth.application.dto.LoginUserRequest;
import com.lfss.libixel.auth.application.dto.LoginUserResponse;
import com.lfss.libixel.auth.application.security.PasswordHasher;
import com.lfss.libixel.auth.application.usecase.LoginUserUseCase;
import com.lfss.libixel.shared.exceptions.InvalidCredentialException;
import com.lfss.libixel.user.domain.User;
import com.lfss.libixel.user.domain.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginUserService implements LoginUserUseCase {

    private final UserRepository repository;
    private final PasswordHasher hasher;

    public LoginUserService (UserRepository repository, PasswordHasher hasher) {
        this.repository = repository;
        this.hasher = hasher;
    }

    public LoginUserResponse execute(LoginUserRequest request) {
        User user = repository.findByUsername(request.username())
                .orElseThrow(InvalidCredentialException::new);

        if (!hasher.match(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialException();
        }

        return new LoginUserResponse(user.getId(), user.getUsername());
    }
}
