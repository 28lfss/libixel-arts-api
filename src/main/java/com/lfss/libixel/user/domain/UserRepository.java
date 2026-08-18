package com.lfss.libixel.user.domain;

import java.util.Optional;

public interface UserRepository {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User save(User user);

    Optional<User> findByUsername(String username);
}
