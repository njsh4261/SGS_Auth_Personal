package com.personalproject.integrated.repository;

import com.personalproject.integrated.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
}
