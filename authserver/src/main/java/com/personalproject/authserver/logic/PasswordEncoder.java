package com.personalproject.authserver.logic;

import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {
    public String encode(String rawPassword) {
        return rawPassword;
    }
}
