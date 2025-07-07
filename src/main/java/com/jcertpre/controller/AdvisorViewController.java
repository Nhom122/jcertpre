package com.jcertpre.controller;

import com.jcertpre.model.Course;
import com.jcertpre.model.User;
import com.jcertpre.services.InstructorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/instructor")
public class AdvisorViewController {

    @Autowired
    private InstructorService instructorService;

    /*// Trang dashboard giảng viên
    @GetMapping("/dashboard")
    public String showAdvisorDashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRole() != User.Role.ADVISOR) {
            return "redirect:/login";
        }

        model.addAttribute("user", currentUser);
        return "Giangvien_Dashboard"; // Tên file HTML
    }

    // Giao diện quản lý khóa học
    @GetMapping("/courses")
    public String showCourseList(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRole() != User.Role.INSTRUCTOR) {
            return "redirect:/login";
        }

        List<Course> courses = instructorService.getCoursesByInstructor(currentUser.getId());
        model.addAttribute("courses", courses);
        return "Giangvien_Quanlikhoahoc"; // Tên file Thymeleaf: instructor_courses.html
    }*/

    // Giao diện tạo khóa học
    @GetMapping("/courses/create")
    public String showCreateCourseForm(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRole() != User.Role.ADVISOR) {
            return "redirect:/login";
        }

        // Nếu cần truyền mẫu Course rỗng cho form
        model.addAttribute("course", new Course());
        return "create_course"; // Tên file HTML: create_course.html
    }

    // Giao diện xem câu hỏi học viên
    @GetMapping("/feedbacks")
    public String showFeedbackPage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRole() != User.Role.ADVISOR) {
            return "redirect:/login";
        }

        // Tuỳ bạn muốn truyền danh sách feedback hay không
        return "feedback_list"; // Tên file HTML: feedback_list.html
    }

    // Giao diện lên lịch livestream
    @GetMapping("/livestreams/schedule")
    public String showLivestreamSchedulePage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRole() != User.Role.ADVISOR) {
            return "redirect:/login";
        }

        return "schedule_livestream"; // Tên file HTML
    }
}
