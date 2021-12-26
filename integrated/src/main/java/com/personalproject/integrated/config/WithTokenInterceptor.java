package com.personalproject.integrated.config;

import com.personalproject.integrated.logic.role.UserRole;
import com.personalproject.integrated.logic.token.JsonWebToken;
import com.personalproject.integrated.logic.token.TokenCookie;
import com.personalproject.integrated.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class WithTokenInterceptor implements HandlerInterceptor {
    private final RedisService redisService;
    private final JsonWebToken jsonWebToken;
    private final TokenCookie tokenCookie;

    @Value("${personal-project.url.auth}")
    private String authServerUrl;

    @Autowired
    public WithTokenInterceptor(RedisService redisService, JsonWebToken jsonWebToken, TokenCookie tokenCookie, UserRole userRole) {
        this.redisService = redisService;
        this.jsonWebToken = jsonWebToken;
        this.tokenCookie = tokenCookie;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // access token should exist in cookie
        String accessToken = tokenCookie.getAccessToken(request);
        if(accessToken != null) {
            // access token should be valid
            if(jsonWebToken.verifyToken(accessToken, response)) {
                // proceed if token is valid
                return true;
            }
            // remove invalid token from cookie
            tokenCookie.removeToken(response);
        }
        // remove token in cache server if exists
        redisService.removeToken();

        // go to login pages
        response.sendRedirect(authServerUrl);
        return false;
    }
}

