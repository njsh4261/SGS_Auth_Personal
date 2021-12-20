package com.personalproject.authserver.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class PasswordEncoder {
    @Value("${personal-project.hashing.algorithm}")
    private String algorithm;

    @Value("${personal-project.hashing.salt}")
    private String salt;

    private Logger logger = LoggerFactory.getLogger(PasswordEncoder.class);

    public String encode(String rawPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(salt.getBytes());
            return String.format("%064x", new BigInteger(1, md.digest(rawPassword.getBytes())));
        } catch (NoSuchAlgorithmException e) {
            logger.info(e.getMessage());
            return null; // todo: 사용자에게 password가 제대로 설정되지 않았음을 알려야 함
        }
    }
}
