package com.personalproject.integrated.repository;

import com.personalproject.integrated.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findOneByEmail(String email);
    Optional<User> findOneByEmailAndPassword(String email, String password);
}
