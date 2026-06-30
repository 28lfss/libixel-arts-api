package com.lfss.libixel.user.domain;

public interface UserRepository {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User save(User user);
}
