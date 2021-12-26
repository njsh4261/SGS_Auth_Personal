package com.personalproject.integrated.controller;

import com.personalproject.integrated.entity.User;
import com.personalproject.integrated.exception.CannotDeleteSelfException;
import com.personalproject.integrated.exception.NoPermissionException;
import com.personalproject.integrated.logic.role.UserRole;
import com.personalproject.integrated.service.UserManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class UserManageController {
    private final UserManageService userManageService;
    private final UserRole userRole;

    @Autowired
    public UserManageController(UserManageService userManageService, UserRole userRole) {
        this.userManageService = userManageService;
        this.userRole = userRole;
    }

    @GetMapping("/admin/user-view")
    public String showOneUserInfo(@RequestParam Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            userRole.checkCurrentUserNotAdmin();
            model.addAttribute("user", userManageService.getUserById(id));
            return "/user-view";
        } catch(RuntimeException e) {
            redirectAttributes.addFlashAttribute("error_msg", e.getMessage());
            return "redirect:/admin";
        }
    }

    @PutMapping("/admin/edit-user/{id}")
    public String editSelectedUsers(@PathVariable("id") Long id, User user, RedirectAttributes redirectAttributes) {
        try {
            userRole.checkCurrentUserNotAdmin();
            user.setId(id);
            userManageService.updateUser(user);
        } catch(RuntimeException e) {
            redirectAttributes.addFlashAttribute("error_msg", e.getMessage());
        }
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/delete-user/{id}")
    public String deleteSelectedUsers(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userRole.checkCurrentUserNotAdmin();
            userRole.checkDeletingSelf(id);
            userManageService.deleteUserById(id);
        } catch(RuntimeException e) {
            redirectAttributes.addFlashAttribute("error_msg", e.getMessage());
        }
        return "redirect:/admin";
    }
}
