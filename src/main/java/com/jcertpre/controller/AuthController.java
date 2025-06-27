package com.jcertpre.controller;

import com.jcertpre.model.User;
import com.jcertpre.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    //Đăng ký người học (Learner)
    @PostMapping("/register/learner")
    public User registerLearner(@RequestParam String email,
                                @RequestParam String password,
                                @RequestParam String fullName) {
        return userService.registerLearner(email, password, fullName);
    }

    //đăng nhập
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


    //đăng xuất
    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/");
    }

}
