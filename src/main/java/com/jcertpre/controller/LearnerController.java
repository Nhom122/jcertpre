package com.jcertpre.controller;

import com.jcertpre.model.*;
import com.jcertpre.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.jcertpre.model.User.Role.LEARNER;

@Controller
@RequestMapping("/courses")
public class LearnerController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService  enrollmentService;

    @Autowired
    private InstructorService  instructorService;

    // 👉 Hiển thị danh sách tất cả khóa học để học viên chọn
    @GetMapping("/learner/register")
    public String showAvailableCourses(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || user.getRole() != LEARNER) {
            return "redirect:/login";
        }

        List<Course> allCourses = courseService.getApprovedCourses();
        List<Course> enrolledCourses = enrollmentService.getCoursesByLearner(user.getId());

        // Lọc ra những khóa học học viên chưa đăng ký
        allCourses.removeAll(enrolledCourses);

        model.addAttribute("enrolledCourses", enrolledCourses);
        model.addAttribute("availableCourses", allCourses);
        return "L4_GiaoDienDangKiKhoaHoc";
    }

    @PostMapping("/learner/register")
    public String registerCourse(@RequestParam("courseId") Long courseId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || user.getRole() != LEARNER) {
            return "redirect:/login";
        }

        Course course = courseService.findById(courseId);
        if (course == null) {
            redirectAttributes.addFlashAttribute("error", "Khóa học không tồn tại.");
            return "redirect:/courses/learner/register";
        }

        enrollmentService.enrollLearnerToCourse(user, course); // Bạn cần có phương thức này
        redirectAttributes.addFlashAttribute("message", "Đăng ký khóa học thành công!");
        return "redirect:/courses/learner/register";
    }

    @GetMapping("/{id}")
    public String viewLessonsByCourse(@PathVariable Long id,
                                      HttpSession session,
                                      Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        // ✅ Kiểm tra người dùng đăng nhập và là học viên
        if (user == null || user.getRole() != LEARNER) {
            return "redirect:/login";
        }

        Course course = courseService.findById(id);
        if (course == null) {
            return "redirect:/courses/register";
        }

        // ✅ Lấy danh sách bài giảng thuộc khóa học
        List<Lesson> lessons = instructorService.getLessonsByCourse(id);

        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        return "Learner_ViewLessons";
    }

}
