package com.personalproject.integrated.service;

import com.personalproject.integrated.entity.User;
import com.personalproject.integrated.repository.UserManageRepository;
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
