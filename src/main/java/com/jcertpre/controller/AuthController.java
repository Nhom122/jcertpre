package com.jcertpre.controller;

import com.jcertpre.model.User;
import com.jcertpre.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // 👉 Hiển thị trang đăng nhập
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Sai email hoặc mật khẩu");
        }
        return "login"; // login.html trong /templates
    }

    // 👉 Hiển thị trang đăng ký
    @GetMapping("/register")
    public String registerPage() {
        return "register"; // register.html trong /templates
    }

    // ✅ Xử lý đăng ký Learner
    @PostMapping("/register/learner")
    public RedirectView registerLearner(@RequestParam String email,
                                        @RequestParam String password,
                                        @RequestParam String fullName,
                                        RedirectView redirectView) {
        userService.registerLearner(email, password, fullName);
        return new RedirectView("/auth/login"); // sau khi đăng ký xong chuyển đến login
    }

    // ✅ Đăng nhập
    @PostMapping("/login")
    public RedirectView login(@RequestParam String email,
                              @RequestParam String password,
                              HttpSession session) {
        try {
            User user = userService.loginUser(email, password);
            session.setAttribute("currentUser", user);

            return switch (user.getRole()) {
                case LEARNER -> new RedirectView("/learner/dashboard");
                case ADMIN -> new RedirectView("/admin/dashboard");
                case INSTRUCTOR -> new RedirectView("/teacher/dashboard");
                case ADVISOR -> new RedirectView("/advisor/dashboard");
                default -> new RedirectView("/unknown-role");
            };
        } catch (RuntimeException e) {
            return new RedirectView("/auth/login?error=true");
        }
    }

    // ✅ Đăng xuất
    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/auth/login");
    }
}
