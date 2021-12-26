package com.personalproject.integrated.controller;

import com.personalproject.integrated.dto.LoginDto;
import com.personalproject.integrated.dto.UserDto;
import com.personalproject.integrated.exception.EmailOrPasswordNotMatchException;
import com.personalproject.integrated.exception.UserAlreadyExistException;
import com.personalproject.integrated.logic.token.TokenCookie;
import com.personalproject.integrated.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String signIn(Model model, LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            authService.signIn(loginDto, response);
            return "redirect:/admin";
        } catch(EmailOrPasswordNotMatchException e) {
            model.addAttribute("error_msg", e.getMessage());
            return "signin";
        }
    }

    @PostMapping("/auth/signup")
    public String signUp(Model model, UserDto userDto) {
        try {
            authService.signUp(userDto);
            return "redirect:/signin"; // return to sign-in page
        } catch(UserAlreadyExistException e) {
            model.addAttribute("error_msg", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/signout")
    public String signOut(HttpServletResponse response) {
        authService.signOut(response);
        return "redirect:/signin"; // return to sign-in page
    }
}
