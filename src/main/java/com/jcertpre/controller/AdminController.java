package com.jcertpre.controller;

import com.jcertpre.model.User;
import com.jcertpre.services.CourseService;
import com.jcertpre.services.FeedbackService;
import com.jcertpre.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private FeedbackService feedbackService;

    // ✅ Trang quản lý người dùng
    @GetMapping("/users")
    public String showUserManagementPage(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "Admin_quanli"; // Trang quản lý người dùng
    }

    // ✅ Tạo người dùng mới (từ admin)
    @PostMapping("/users/create")
    public String createUser(@ModelAttribute User user, Model model) {
        try {
            userService.createUser(user); // Cần có trong UserService
            model.addAttribute("message", "Tạo người dùng thành công!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // ✅ Cập nhật người dùng
    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user, Model model) {
        userService.updateUser(id, user);
        model.addAttribute("message", "Cập nhật người dùng thành công!");
        return "redirect:/admin/users";
    }

    // ✅ Xóa người dùng
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, Model model) {
        userService.deleteUser(id);
        model.addAttribute("message", "Xóa người dùng thành công!");
        return "redirect:/admin/users";
    }
}