package com.personalproject.authserver.controller;

import com.personalproject.authserver.dto.LoginDto;
import com.personalproject.authserver.dto.UserDto;
import com.personalproject.authserver.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final AuthService authService;
    private final String adminServerUrl = "http://localhost:8081";

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
        // if user is already signed in, redirect to admin page
        return "signin";
    }

    @GetMapping("/signup")
    public String showSignUpPage(@ModelAttribute UserDto userDto) {
        // if user is already signed in, redirect to admin page
        return "signup";
    }

    @PostMapping("/auth/signin")
    public String signIn(LoginDto loginDto) {
        // validate user's info
        // if valid, create access & refresh token, save it into cache server, and hand it to the user
        // then redirect to admin page of the admin server
        // if the user is already signed in or invalid info is given, response as bad request
        return "redirect:" + adminServerUrl;
    }

    @PostMapping("/auth/signup")
    public String signUp(UserDto userDto) {
        // add new user to DB
        authService.signUp(userDto);
        // return to sign in page
        // if the user is already signed in or invalid info is given, show error message
        return "redirect:/signin";
    }

    @ResponseBody
    @DeleteMapping("/auth/signout")
    public String signOut() {
        // delete user's access & refresh token from cache server
        // if the user is already signed out or invalid info is given, response as bad request
        return "sign out";
    }
}
