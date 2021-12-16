package com.personalproject.authserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/")
    public String intro() {
        return "redirect:/signin";
    }

    @GetMapping("/signin")
    public String signIn() {
        return "signin";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }
}
