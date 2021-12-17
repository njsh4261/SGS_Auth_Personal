package com.personalproject.adminserver.service;

import com.personalproject.adminserver.domain.User;
import com.personalproject.adminserver.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    public List<User> getUserList() {
        return adminRepository.findAll();
    }
}
