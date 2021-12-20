package com.personalproject.adminserver.config;

import com.personalproject.adminserver.logic.JsonWebToken;
import com.personalproject.adminserver.logic.TokenCookie;
import com.personalproject.adminserver.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private RedisService redisService;

    @Value("${personal-project.url.auth}")
    private String authServerUrl;

    @Autowired
    private JsonWebToken jsonWebToken;

    @Autowired
    private TokenCookie tokenCookie;

    @Autowired
    public AuthInterceptor(RedisService redisService) {
        this.redisService = redisService;
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
            // remove token from cookie
            tokenCookie.removeToken(response);
        }
        // remove token in cache server if exists
        redisService.removeToken();

        // go to login pages
        response.sendRedirect(authServerUrl);
        return false;
    }
}
