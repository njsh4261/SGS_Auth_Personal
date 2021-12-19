package com.personalproject.adminserver.repository;

import com.personalproject.adminserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserManageRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    User save(User user);
    void deleteById(User user);
}
