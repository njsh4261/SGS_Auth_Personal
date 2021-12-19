package com.personalproject.adminserver.repository;

import com.personalproject.adminserver.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
}
