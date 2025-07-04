package com.jcertpre.controller;

import com.jcertpre.model.ConsultationSchedule;
import com.jcertpre.model.User;
import com.jcertpre.services.ConsultationScheduleService;
import com.jcertpre.services.CourseService;
import com.jcertpre.services.FeedbackService;
import com.jcertpre.services.UserService;
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

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private FeedbackService feedbackService;

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
    public String adminDashboard(Model model) {
        long totalUsers = userService.getAllUsers().size();
        long pendingCourses = courseService.countPendingCourses(); // cần triển khai
        long pendingFeedbacks = feedbackService.countPendingFeedbacks(); // cần triển khai

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("pendingCourses", pendingCourses);
        model.addAttribute("pendingFeedbacks", pendingFeedbacks);

        return "Admin_trangchu";
    }

    @GetMapping("/instructor/dashboard")
    public String instructorDashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRole() != INSTRUCTOR) {
            return "redirect:/login";
        }

        model.addAttribute("user", currentUser);
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
