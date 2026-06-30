package com.lfss.libixel.auth.application.service;

import com.lfss.libixel.auth.application.dto.RegisterUserRequest;
import com.lfss.libixel.auth.application.dto.RegisterUserResponse;
import com.lfss.libixel.auth.application.mapper.RegisterMapper;
import com.lfss.libixel.auth.application.security.PasswordHasher;
import com.lfss.libixel.auth.application.usecase.RegisterUserUseCase;
import com.lfss.libixel.shared.exceptions.EmailAlreadyUsedException;
import com.lfss.libixel.shared.exceptions.UsernameAlreadyUsedException;
import com.lfss.libixel.user.domain.User;
import com.lfss.libixel.user.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository repository;
    private final RegisterMapper mapper;
    private final PasswordHasher hasher;

    public RegisterUserService(UserRepository repository, RegisterMapper mapper, PasswordHasher hasher) {
        this.repository = repository;
        this.mapper = mapper;
        this.hasher = hasher;
    }

    @Override
    public RegisterUserResponse execute(RegisterUserRequest request) {
        if (repository.existsByUsername(request.username())) {
            throw new UsernameAlreadyUsedException("Username is already in use.");
        }

        if (repository.existsByEmail(request.email())) {
            throw new EmailAlreadyUsedException("Email is already in use.");
        }

        final User newUser = User.register(
                request.username(),
                request.email(),
                hasher.hash(request.password())
        );
        User savedUser = repository.save(newUser);
        return mapper.toResponse(savedUser);
    }
}
