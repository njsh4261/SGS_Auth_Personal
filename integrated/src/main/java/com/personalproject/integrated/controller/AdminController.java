package com.personalproject.integrated.controller;

import com.personalproject.integrated.entity.User;
import com.personalproject.integrated.logic.role.UserRole;
import com.personalproject.integrated.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    private final AdminService adminService;
    private final UserRole userRole;
    private final int pageSize = 10;

    @Autowired
    public AdminController(AdminService adminService, UserRole userRole) {
        this.adminService = adminService;
        this.userRole = userRole;
    }

    @GetMapping("/admin")
    public String adminPage(Model model, @PageableDefault(size = pageSize) Pageable pageable) {
        Page<User> userList = adminService.getUserList(pageable);

        int startPage = Math.max(1, userList.getPageable().getPageNumber()-4);
        int endPage = Math.min(userList.getTotalPages(), userList.getPageable().getPageNumber()+4);

        model.addAttribute("userList", userList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("currentUserRole", userRole.getRole());
        return "admin";
    }
}
