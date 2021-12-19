package com.personalproject.authserver.repository;

import com.personalproject.authserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByEmail(String email);
}
