package com.personalproject.adminserver.service;

import com.personalproject.adminserver.entity.User;
import com.personalproject.adminserver.repository.UserManageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserManageService {
    private final UserManageRepository userManageRepository;

    @Autowired
    public UserManageService(UserManageRepository userManageRepository) {
        this.userManageRepository = userManageRepository;
    }

    public User getUserById(Long id) {
        return userManageRepository.getById(id);
    }

    @Transactional
    public User updateUser(User user) {
        return userManageRepository.save(user);
    }

    @Transactional
    public void deleteUserById(Long id) {
        userManageRepository.deleteById(id);
    }
}
