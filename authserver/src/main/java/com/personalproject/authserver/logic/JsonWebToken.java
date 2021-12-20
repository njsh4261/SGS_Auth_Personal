package com.personalproject.authserver.logic;

import com.personalproject.authserver.entity.User;
import com.personalproject.authserver.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
public class JsonWebToken {
    @Autowired
    private RedisService redisService;

    @Value("${personal-project.jwt.secret}")
    private String secret;

    @Value("${personal-project.jwt.lifespan-millisecond.access-token}")
    private long accessTokenLifespan;

    @Value("${personal-project.jwt.lifespan-millisecond.refresh-token}")
    private long refreshTokenLifespan;

    private final Logger logger = LoggerFactory.getLogger(PasswordEncoder.class);

    public String createToken(User user, TokenType tokenType) {
        long tokenLifespan = (tokenType == TokenType.ACCESS) ? accessTokenLifespan : refreshTokenLifespan;
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("type", tokenType)
                .setExpiration(new Date((new Date()).getTime() + tokenLifespan))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean verifyToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody();
            String tokenCached = redisService.fetchToken();
            if(claims.getExpiration().before(new Date()) && Objects.equals(token, tokenCached)) {
                return true;
            }
        } catch(Exception e) {
            logger.info(e.getMessage());
        }
        return false;
    }
}
