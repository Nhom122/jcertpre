package com.jcertpre.controller;

import com.jcertpre.model.ConsultationSchedule;
import com.jcertpre.model.User;
import com.jcertpre.services.ConsultationScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.List;

import static com.jcertpre.model.User.Role.*;


@Controller
public class DashboardController {
    @Autowired
    private ConsultationScheduleService scheduleService;

    @GetMapping("/learner/dashboard")
    public String learnerDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || LEARNER != user.getRole()) {
            return "login";
        }

        model.addAttribute("user", user);

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
    public String advisorDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "login";
        }

        if (ADVISOR != user.getRole()) {
            return "login";
        }

        List<ConsultationSchedule> pendingSchedules = scheduleService.getPendingByAdvisor(user);
        model.addAttribute("pendingConsultations", pendingSchedules.size());
        model.addAttribute("pendingList", pendingSchedules);

        return "CoVanTrangtru";
    }
}
