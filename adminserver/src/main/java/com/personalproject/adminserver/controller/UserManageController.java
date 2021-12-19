package com.personalproject.adminserver.controller;

import com.personalproject.adminserver.entity.User;
import com.personalproject.adminserver.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserManageController {
    private final UserManageService userManageService;

    @Autowired
    public UserManageController(UserManageService userManageService) {
        this.userManageService = userManageService;
    }

    @GetMapping("/admin/user-view")
    public String showOneUserInfo(Model model, @RequestParam Long id) {
        User user = userManageService.getUserById(id);
        model.addAttribute("user", user);
        return "/user-view";
    }

    @PutMapping("/admin/edit-user/{id}")
    public String editSelectedUsers(User user, @PathVariable("id") Long id) {
        user.setId(id);
        userManageService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/delete-user/{id}")
    public String deleteSelectedUsers(@PathVariable("id") Long id) {
        userManageService.deleteUserById(id);
        return "redirect:/admin";
    }
}
