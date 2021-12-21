package com.personalproject.integrated.service;

import com.personalproject.integrated.dto.LoginDto;
import com.personalproject.integrated.dto.UserDto;
import com.personalproject.integrated.entity.User;
import com.personalproject.integrated.exception.EmailOrPasswordNotMatchException;
import com.personalproject.integrated.exception.UserAlreadyExistException;
import com.personalproject.integrated.logic.JsonWebToken;
import com.personalproject.integrated.logic.PasswordEncoder;
import com.personalproject.integrated.logic.TokenCookie;
import com.personalproject.integrated.logic.TokenType;
import com.personalproject.integrated.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final JsonWebToken jsonWebToken;
    private final TokenCookie tokenCookie;

    @Autowired
    public AuthService(AuthRepository authRepository, RedisService redisService, PasswordEncoder passwordEncoder,
                       JsonWebToken jsonWebToken, TokenCookie tokenCookie) {
        this.authRepository = authRepository;
        this.redisService = redisService;
        this.passwordEncoder = passwordEncoder;
        this.jsonWebToken = jsonWebToken;
        this.tokenCookie = tokenCookie;
    }

    @Transactional
    public void signIn(LoginDto loginDto, HttpServletResponse response) {
        Optional<User> user = authRepository.findOneByEmailAndPassword(
                loginDto.getEmail(), passwordEncoder.encode(loginDto.getPassword())
        );

        // validate user's info
        if(!user.isPresent()) {
            throw new EmailOrPasswordNotMatchException("Email or Password is incorrect.");
        }

        // if valid, create access token
        // TODO: refresh token is also required
        String accessToken = jsonWebToken.createToken(user.get(), TokenType.ACCESS);

        // save token into cache server
        redisService.storeToken(accessToken);

        // store token in user's local cookie
        tokenCookie.storeAccessToken(accessToken, response);
    }

    @Transactional
    public void signUp(UserDto userDto) {
        // if the user is already signed in or invalid info is given, show error message
        if(authRepository.findOneByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User already exists.");
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .name(userDto.getName())
                .role("normal_user")
                .build();

        // add new user to DB
        authRepository.save(user);
    }

    @Transactional
    public void signOut(HttpServletResponse response) {
        // delete user's token from local cookie
        tokenCookie.removeToken(response);

        // delete user's token from cache server
        redisService.removeToken();
    }
}
