package com.personalproject.integrated.controller;

import com.personalproject.integrated.entity.User;
import com.personalproject.integrated.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class AdminController {
    private final AdminService adminService;
    private final String loginServerUrl = "http://localhost:8080";
    private final int pageSize = 10;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/")
    public String index(){
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String adminPage(Model model, @PageableDefault(size = pageSize) Pageable pageable) {
        Page<User> userList = adminService.getUserList(pageable);

        int startPage = Math.max(1, userList.getPageable().getPageNumber()-4);
        int endPage = Math.min(userList.getTotalPages(), userList.getPageable().getPageNumber()+4);

        model.addAttribute("userList", userList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "admin";
    }

    @GetMapping("/signout")
    public String signOut(HttpServletResponse response) {
        adminService.signOut(response);
        return "redirect:" + loginServerUrl;
    }
}
