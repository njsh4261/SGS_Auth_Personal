package com.personalproject.authserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    private final String authServerUrl = "http://localhost:8081";

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
    public String showSignUpPage() {
        // if user is already signed in, redirect to admin page
        return "signup";
    }

    @PostMapping("/auth/signin")
    public String signInHandle() {
        // validate user's info
        // if valid, create access & refresh token, save it into cache server, and hand it to the user
        // then redirect to admin page of the admin server
        // if the user is already signed in or invalid info is given, response as bad request
        return "redirect:" + authServerUrl;
    }

    @PostMapping("/auth/signup")
    public String signUpHandle() {
        // add new user to DB
        // if valid, create access & refresh token, save it into cache server, and hand it to the user
        // return to sign in page
        // if the user is already signed in or invalid info is given, response as bad request
        return "redirect:/signin";
    }

    @ResponseBody
    @DeleteMapping("/auth/signout")
    public String signOutHandle() {
        // delete user's access & refresh token from cache server
        // if the user is already signed out or invalid info is given, response as bad request
        return "sign out";
    }
}
