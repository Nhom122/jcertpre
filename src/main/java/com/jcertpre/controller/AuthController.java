package com.jcertpre.controller;

import com.jcertpre.model.User;
import com.jcertpre.services.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Tên file register.html (viết thường)
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, HttpSession session, Model model) {
        boolean success = authService.register(user);
        if (!success) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "register";
        }
        session.setAttribute("user", user);
        return "redirect:/login";
    }
}
