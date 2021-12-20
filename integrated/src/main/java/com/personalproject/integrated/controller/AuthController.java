package com.personalproject.integrated.controller;

import com.personalproject.integrated.dto.LoginDto;
import com.personalproject.integrated.dto.UserDto;
import com.personalproject.integrated.logic.TokenCookie;
import com.personalproject.integrated.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthController {
    private final AuthService authService;
    private final TokenCookie tokenCookie;

    @Value("${personal-project.url.admin}")
    private String adminServerUrl;

    @Autowired
    public AuthController(AuthService authService, TokenCookie tokenCookie) {
        this.authService = authService;
        this.tokenCookie = tokenCookie;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/signin";
    }

    @GetMapping("/signin")
    public String showSignInPage(@ModelAttribute LoginDto loginDto) {
        return "signin";
    }

    @GetMapping("/signup")
    public String showSignUpPage(@ModelAttribute UserDto userDto) {
        return "signup";
    }

    @PostMapping("/auth/signin")
    public String signIn(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        authService.signIn(loginDto, response);
        String token = tokenCookie.getAccessToken(request);
        return "redirect:" + adminServerUrl;
    }

    @PostMapping("/auth/signup")
    public String signUp(UserDto userDto) {
        authService.signUp(userDto);
        return "redirect:/signin"; // return to sign-in page
    }

    @ResponseBody
    @DeleteMapping("/auth/signout")
    public String signOut(HttpServletResponse response) {
        authService.signOut(response);
        return "sign out";
    }
}
