package com.personalproject.authserver.config;

import com.personalproject.authserver.logic.JsonWebToken;
import com.personalproject.authserver.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private RedisService redisService;

    @Value("${personal-project.url.admin}")
    private String adminServerUrl;

    @Autowired
    private JsonWebToken jsonWebToken;

    @Autowired
    public AuthInterceptor(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // access token should exist in cookie
        Cookie accessTokenCookie = WebUtils.getCookie(request, "accessToken");
        if(accessTokenCookie != null) {
            // access token should be valid
            String accessToken = accessTokenCookie.getValue();
            if(jsonWebToken.verifyToken(accessToken)) {
                // if token is valid, redirect to admin server
                response.sendRedirect(adminServerUrl);
                return false;
            }
            // remove token from cookie
            accessTokenCookie.setMaxAge(0);
        }
        // remove token in cache server if exists
        redisService.removeToken();

        // go to login pages
        return true;
    }
}
