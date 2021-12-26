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
    private RedisService redisService;

    @Value("${personal-project.url.admin}")
    private String adminServerUrl;

    @Autowired
    private JsonWebToken jsonWebToken;

    @Autowired
    private TokenCookie tokenCookie;

    @Autowired
    public WithoutTokenInterceptor(RedisService redisService) {
        this.redisService = redisService;
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
