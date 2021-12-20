package com.personalproject.authserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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

    public String getClientIp() {
        // reference: https://www.lesstif.com/java/spring-client-ip-18220218.html
        // reference 2: https://jaehun2841.github.io/2018/08/10/2018-08-10-httprequest-client-ip/#httpservletrequest%EC%97%90%EC%84%9C-ip-%EA%B5%AC%ED%95%98%EA%B8%B0
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getHeader("X-FORWARDED-FOR");
        if(!StringUtils.hasText(ip)) {
            if ("unknown".equalsIgnoreCase(ip)) {
                ip = req.getHeader("Proxy-Client-IP");
            }
            if ("unknown".equalsIgnoreCase(ip)) {
                ip = req.getHeader("WL-Proxy-Client-IP");
            }
            if ("unknown".equalsIgnoreCase(ip)) {
                ip = req.getHeader("HTTP_CLIENT_IP");
            }
            if ("unknown".equalsIgnoreCase(ip)) {
                ip = req.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if ("unknown".equalsIgnoreCase(ip)) {
                ip = req.getRemoteAddr();
            }
        }
        return ip;
    }

    public void storeToken(String token){
        redisTemplate.opsForValue().set(this.getClientIp(), token, Duration.ofMillis(accessTokenLifespan));
    }

    public String fetchToken() {
        return redisTemplate.opsForValue().get(this.getClientIp());
    }

    public void removeToken() {
        redisTemplate.delete(this.getClientIp());
    }
}
