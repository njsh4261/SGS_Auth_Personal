package com.personalproject.adminserver.service;

import com.personalproject.adminserver.entity.User;
import com.personalproject.adminserver.logic.TokenCookie;
import com.personalproject.adminserver.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletResponse;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final TokenCookie tokenCookie;
    private final RedisService redisService;

    @Value("${personal-project.url.auth}")
    private String authServerUrl;

    @Autowired
    public AdminService(AdminRepository adminRepository, TokenCookie tokenCookie, RedisService redisService){
        this.adminRepository = adminRepository;
        this.tokenCookie = tokenCookie;
        this.redisService = redisService;
    }

    public Page<User> getUserList(Pageable pageable) {
        return adminRepository.findAll(pageable);
    }

    @Transactional
    public void signIn(String accessToken, HttpServletResponse response) {
        tokenCookie.storeAccessToken(accessToken, response);
    }

    @Transactional
    public void signOut(HttpServletResponse response) {
        WebClient webClient = WebClient.create(authServerUrl);
        webClient.delete()
                .uri("/auth/signout")
                .retrieve().toBodilessEntity().block();

        // delete user's token from local cookie
        tokenCookie.removeToken(response);

        // delete user's token from cache server
        redisService.removeToken();
    }
}
