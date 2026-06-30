package com.lfss.libixel.auth.application.mapper;

import com.lfss.libixel.auth.application.dto.RegisterUserResponse;
import com.lfss.libixel.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {

    public RegisterUserResponse toResponse(User user) {
        return new RegisterUserResponse(user.getId(), user.getUsername(), user.getEmail());
    }
}
