package com.personalproject.adminserver.controller;

import com.personalproject.adminserver.dto.LoginTokenDto;
import com.personalproject.adminserver.entity.User;
import com.personalproject.adminserver.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
public class AdminController {
    private final AdminService adminService;

    @Value("${personal-project.url.auth}")
    private String authServerUrl;

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

    @Transactional
    @ResponseBody
    @PostMapping("/signin")
    @CrossOrigin("${personal-project.url.auth}")
    public String signIn(@RequestBody LoginTokenDto loginTokenDto, HttpServletResponse response) {
        adminService.signIn(loginTokenDto.getToken(), response);
        return "Sign in: token is stored";
    }

    @Transactional
    @GetMapping("/signout")
    public String signOut(HttpServletResponse response) {
        adminService.signOut(response);
        return "redirect:" + authServerUrl;
    }
}
