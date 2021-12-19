package com.personalproject.adminserver.service;

import com.personalproject.adminserver.entity.User;
import com.personalproject.adminserver.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    public Page<User> getUserList(Pageable pageable) {
        return adminRepository.findAll(pageable);
    }
}
