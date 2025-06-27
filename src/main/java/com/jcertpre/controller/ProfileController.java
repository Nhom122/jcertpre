package com.jcertpre.controller;

import com.jcertpre.model.User;
import com.jcertpre.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    // Hiển thị trang hồ sơ cá nhân
    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", currentUser);
        return "profile"; // Giao diện profile.html
    }

    // Cập nhật thông tin hồ sơ cá nhân
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("user") User updatedUser,
                                HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        // Cập nhật thông tin trong DB và session
        currentUser.setFullName(updatedUser.getFullName());
        currentUser.setEmail(updatedUser.getEmail());

        userService.updateUser(currentUser.getId(), currentUser);
        session.setAttribute("user", currentUser);
        model.addAttribute("user", currentUser);
        model.addAttribute("message", "Cập nhật hồ sơ thành công!");

        return "profile";
    }
}
