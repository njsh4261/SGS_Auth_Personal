package com.personalproject.adminserver.logic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenCookie {
    @Value("${personal-project.cookie.access-token}")
    private String accessTokenCookieName;

    @Value("${personal-project.cookie.refresh-token}")
    private String refreshTokenCookieName;

    private void storeToken(String token, String cookieName, HttpServletResponse response){
        Cookie accessTokenCookie = new Cookie(cookieName, token);
        response.addCookie(accessTokenCookie);
    }

    public void storeAccessToken(String token, HttpServletResponse response) {
        storeToken(token, accessTokenCookieName, response);
    }

    public void storeRefreshToken(String token, HttpServletResponse response) {
        storeToken(token, refreshTokenCookieName, response);
    }

    private Cookie getAccessTokenCookie(HttpServletRequest request) {
        return WebUtils.getCookie(request, "accessToken");
    }

    public String getAccessToken(HttpServletRequest request) {
        Cookie accessTokenCookie = getAccessTokenCookie(request);
        if(accessTokenCookie == null) {
            return null;
        }
        return getAccessTokenCookie(request).getValue();
    }

    public void removeToken(HttpServletResponse response) {
        Cookie cookie = new Cookie(accessTokenCookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
