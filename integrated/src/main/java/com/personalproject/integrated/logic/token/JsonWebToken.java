package com.personalproject.integrated.logic.token;

import com.personalproject.integrated.entity.User;
import com.personalproject.integrated.logic.role.UserRole;
import com.personalproject.integrated.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class JsonWebToken {
    private RedisService redisService;
    private TokenCookie tokenCookie;
    private UserRole userRole;

    @Autowired
    public JsonWebToken(RedisService redisService, TokenCookie tokenCookie, UserRole userRole) {
        this.redisService = redisService;
        this.tokenCookie = tokenCookie;
        this.userRole = userRole;
    }

    @Value("${personal-project.jwt.secret}")
    private String secret;

    @Value("${personal-project.jwt.lifespan-millisecond.access-token}")
    private long accessTokenLifespan;

    @Value("${personal-project.jwt.lifespan-millisecond.refresh-token}")
    private long refreshTokenLifespan;

    private final Logger logger = LoggerFactory.getLogger(JsonWebToken.class);

    public String createToken(User user, TokenType tokenType) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("type", tokenType)
                .claim("userId", user.getId())
                .setExpiration(new Date((new Date()).getTime() + getTokenLifeSpan(tokenType)))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean verifyToken(String token, HttpServletResponse response) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            String tokenCached = redisService.getToken();
            if(claims.getExpiration().after(new Date()) && token.equals(tokenCached)) {
                // update user's role info with user id inside token
                userRole.updateCurrentUserRole(((Number) claims.get("userId")).longValue());

                // if token is valid, update its expiration and store it in both client cookie and cache server
                String updatedToken = updateTokenExpiration(claims);
                redisService.storeToken(updatedToken);
                tokenCookie.storeAccessToken(updatedToken, response);
                return true;
            }
        } catch(Exception e) {
            logger.info(e.getMessage());
        }
        return false;
    }

    private long getTokenLifeSpan(TokenType tokenType) {
        return (tokenType == TokenType.ACCESS) ? accessTokenLifespan : refreshTokenLifespan;
    }

    private String updateTokenExpiration(Claims claims) {
        TokenType tokenType = TokenType.valueOf((String) claims.get("type"));
        return Jwts.builder()
                .setSubject(claims.getSubject())
                .claim("type", tokenType)
                .claim("userId", claims.get("userId"))
                .setExpiration(new Date((new Date()).getTime() + getTokenLifeSpan(tokenType)))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
}
