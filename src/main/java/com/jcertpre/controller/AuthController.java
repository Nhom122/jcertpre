package com.jcertpre.controller;

import com.jcertpre.model.User;
import com.jcertpre.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // 👉 Hiển thị trang đăng nhập
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html trong /templates
    }

    // 👉 Hiển thị trang đăng ký
    @GetMapping("/register")
    public String registerPage() {
        return "register"; // register.html trong /templates
    }

    // ✅ Xử lý đăng ký Learner
    @PostMapping("/api/register/learner")
    public String registerLearner(@RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String fullName,
                                  Model model) {
        try {
            userService.registerLearner(email, password, fullName);
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    // ✅ Đăng nhập
    @PostMapping("/api/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        try {
            User user = userService.loginUser(email, password);
            session.setAttribute("currentUser", user);

            return switch (user.getRole()) {
                case LEARNER -> "redirect:/learner/dashboard";
                case ADMIN -> "redirect:/admin/dashboard";
                case INSTRUCTOR -> "redirect:/teacher/dashboard";
                case ADVISOR -> "redirect:/advisor/dashboard";
            };
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    // ✅ Đăng xuất
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // 👉 Hiển thị trang đăng nhập riêng cho Admin
    @GetMapping("/admin/login")
    public String adminLoginPage() {
        return "Admin_login"; // Tên file HTML: Admin_login.html
    }

    // ✅ Xử lý đăng nhập Admin riêng
    @PostMapping("/api/login/admin")
    public String adminLogin(@RequestParam String email,
                             @RequestParam String password,
                             HttpSession session,
                             Model model) {
        try {
            User user = userService.loginUser(email, password);

            // Kiểm tra vai trò có phải ADMIN không
            if (user.getRole() != User.Role.ADMIN) {
                model.addAttribute("error", "Bạn không có quyền truy cập trang admin.");
                return "Admin_login";
            }

            session.setAttribute("currentUser", user);
            return "redirect:/admin/dashboard";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "Admin_login";
        }
    }


}