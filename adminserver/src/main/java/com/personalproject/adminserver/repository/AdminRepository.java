package com.personalproject.adminserver.repository;

import com.personalproject.adminserver.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<User, Long> {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    List<User> findAll();
    void delete(User user);
}
