package com.personalproject.integrated.service;

import com.personalproject.integrated.entity.User;
import com.personalproject.integrated.logic.TokenCookie;
import com.personalproject.integrated.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final TokenCookie tokenCookie;
    private final RedisService redisService;

    @Autowired
    public AdminService(AdminRepository adminRepository, TokenCookie tokenCookie, RedisService redisService){
        this.adminRepository = adminRepository;
        this.tokenCookie = tokenCookie;
        this.redisService = redisService;
    }

    public Page<User> getUserList(Pageable pageable) {
        return adminRepository.findAll(pageable);
    }

    public void signOut(HttpServletResponse response) {
        // delete user's token from local cookie
        tokenCookie.removeToken(response);

        // delete user's token from cache server
        redisService.removeToken();
    }
}
