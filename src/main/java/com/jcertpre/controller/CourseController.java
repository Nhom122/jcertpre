package com.jcertpre.controller;

import com.jcertpre.model.Course;
import com.jcertpre.model.User;
import com.jcertpre.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    // 👉 Trang danh sách tất cả khóa học (của Instructor)
    @GetMapping("/instructor/courses")
    public String showInstructorCourses(Model model, @SessionAttribute("currentUser") User instructor) {
        List<Course> courseList = courseService.getCoursesByInstructor(instructor);
        model.addAttribute("courses", courseList);
        return "Giangvien_Quanlikhoahoc"; // View đúng tên
    }

    // 👉 Trang quản trị admin: các khóa học chờ duyệt
    @GetMapping("/admin/courses/pending")
    public String showPendingCourses(Model model) {
        List<Course> pendingCourses = courseService.getPendingCourses();
        model.addAttribute("pendingCourses", pendingCourses);
        return "Admin_duyetkhoahoc";
    }

    // ✅ Duyệt khóa học
    @PostMapping("/admin/courses/approve/{id}")
    public String approveCourse(@PathVariable Long id) {
        courseService.approveCourse(id);
        return "redirect:/admin/courses/pending";
    }

    // ✅ Từ chối khóa học với lý do
    @PostMapping("/admin/courses/reject/{id}")
    public String rejectCourse(@PathVariable Long id,
                               @RequestParam("reason") String reason) {
        courseService.rejectCourse(id, reason);
        return "redirect:/admin/courses/pending";
    }

    // ✅ Tạo khóa học mới
    @PostMapping("/courses/create")
    public String createCourse(@RequestParam String title,
                               @RequestParam String description,
                               @RequestParam Course.CourseType courseType,
                               @SessionAttribute("currentUser") User instructor,
                               RedirectAttributes redirectAttributes) {
        Course newCourse = new Course(title, description, courseType, instructor);
        courseService.save(newCourse);
        redirectAttributes.addFlashAttribute("message", "Tạo khóa học thành công!");
        return "redirect:/instructor/courses";
    }

    // ✅ Xóa khóa học
    @PostMapping("/instructor/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id,
                               @SessionAttribute("currentUser") User instructor,
                               RedirectAttributes redirectAttributes) {
        Course course = courseService.findById(id);
        if (course != null && course.getInstructor().getId().equals(instructor.getId())) {
            courseService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Đã xóa khóa học thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa khóa học không thuộc quyền sở hữu.");
        }
        return "redirect:/instructor/courses";
    }
}
