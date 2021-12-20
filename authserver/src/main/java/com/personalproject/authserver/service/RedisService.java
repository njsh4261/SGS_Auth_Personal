package com.personalproject.authserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

@Service
public class RedisService {
    private RedisTemplate<String, String> redisTemplate;

    @Value("${personal-project.jwt.lifespan-millisecond.access-token}")
    private long accessTokenLifespan;

    @Autowired
    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getClientIp() {
        // reference: https://www.lesstif.com/java/spring-client-ip-18220218.html
        HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getHeader("X-FORWARDED-FOR");
        if(ip == null) {
            ip = req.getRemoteAddr();
        }
        return ip;
    }

    public void storeToken(String token){
        redisTemplate.opsForValue().set(this.getClientIp(), token, Duration.ofMillis(accessTokenLifespan));
    }

    public String getToken() {
        return redisTemplate.opsForValue().get(this.getClientIp());
    }

    public void removeToken() {
        redisTemplate.delete(this.getClientIp());
    }
}
