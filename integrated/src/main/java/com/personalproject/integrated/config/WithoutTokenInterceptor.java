package com.personalproject.integrated.config;

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
public class WithoutTokenInterceptor implements HandlerInterceptor {
    private final RedisService redisService;
    private final JsonWebToken jsonWebToken;
    private final TokenCookie tokenCookie;

    @Value("${personal-project.url.admin}")
    private String adminServerUrl;

    @Autowired
    public WithoutTokenInterceptor(RedisService redisService, JsonWebToken jsonWebToken, TokenCookie tokenCookie) {
        this.redisService = redisService;
        this.jsonWebToken = jsonWebToken;
        this.tokenCookie = tokenCookie;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // get token from cookie if exist
        String accessToken = tokenCookie.getAccessToken(request);

        // if token exists and is valid, redirect to admin page
        if(accessToken != null) {
            if(jsonWebToken.verifyToken(accessToken, response)) {
                response.sendRedirect(adminServerUrl);
                return false;
            }
            // remove invalid token from cookie
            tokenCookie.removeToken(response);
        }
        // remove token in cache server if exists
        redisService.removeToken();

        // go to login pages
        return true;
    }
}
