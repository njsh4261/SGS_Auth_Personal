package com.personalproject.authserver.service;

import com.personalproject.authserver.dto.UserDto;
import com.personalproject.authserver.entity.User;
import com.personalproject.authserver.exception.UserAlreadyExistException;
import com.personalproject.authserver.repository.AuthRepository;
import com.personalproject.authserver.logic.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private AuthRepository authRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void signUp(UserDto userDto) {
        if(authRepository.findOneByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("");
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .name(userDto.getName())
                .build();
        authRepository.save(user);
    }
}
