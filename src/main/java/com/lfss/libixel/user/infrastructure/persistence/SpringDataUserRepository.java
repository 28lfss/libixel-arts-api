package com.lfss.libixel.user.infrastructure.persistence;

import com.lfss.libixel.user.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataUserRepository extends CrudRepository<User, UUID> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
}
