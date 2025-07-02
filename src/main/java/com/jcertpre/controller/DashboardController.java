package com.jcertpre.controller;

import com.jcertpre.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;


@Controller
public class DashboardController {

    @GetMapping("/learner/dashboard")
    public String learnerDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "Admin_trangchu";
    }

    @GetMapping("/teacher/dashboard")
    public String instructorDashboard() {
        return "Giangvien_Dashboard";
    }

    @GetMapping("/advisor/dashboard")
    public String advisorDashboard() {
        return "CoVan-Trangtru";
    }
}
