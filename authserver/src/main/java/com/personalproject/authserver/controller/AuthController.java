package com.personalproject.authserver.controller;

import com.personalproject.authserver.dto.LoginDto;
import com.personalproject.authserver.dto.UserDto;
import com.personalproject.authserver.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthController {
    private final AuthService authService;

    @Value("${personal-project.url.admin}")
    private String adminServerUrl;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/signin";
    }

    @GetMapping("/signin")
    public String showSignInPage() {
        return "signin";
    }

    @GetMapping("/signup")
    public String showSignUpPage(@ModelAttribute UserDto userDto) {
        return "signup";
    }

    @PostMapping("/auth/signin")
    public String signIn(LoginDto loginDto, HttpServletResponse response) {
        authService.signIn(loginDto, response);
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
