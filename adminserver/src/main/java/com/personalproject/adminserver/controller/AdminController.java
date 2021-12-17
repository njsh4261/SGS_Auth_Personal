package com.personalproject.adminserver.controller;

import com.personalproject.adminserver.domain.User;
import com.personalproject.adminserver.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {
    private final AdminService adminService;
    private final String loginServerUrl = "http://localhost:8080";

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String index(){
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        List<User> userList = adminService.getUserList();
        model.addAttribute("userList", userList);
        return "admin";
    }

    @GetMapping("/admin/user-view/{userId}")
    public String showOneUserInfo(@PathVariable("userId") Long userId) {
        return "user-view";
    }

    @ResponseBody
    @PutMapping("/admin/edit-user/{userId}")
    public String editSelectedUsers(@PathVariable("userId") Long userId) {
        return "edit user " + userId.toString();
    }

    @ResponseBody
    @DeleteMapping("/admin/delete-user/{userId}")
    public String deleteSelectedUsers(@PathVariable("userId") Long userId) {
        return "delete user " + userId.toString();
    }

    @GetMapping("/admin/signout")
    public String signOut() {
        // send 'DELETE /auth/signout' to login server
        // if response status is 200 ok, redirect to sign in page
        // if something trouble with logging out, stay in the page
        return "redirect:" + loginServerUrl;
    }
}
