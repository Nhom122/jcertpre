package com.jcertpre.controller;

import com.jcertpre.model.Course;
import com.jcertpre.model.Lesson;
import com.jcertpre.model.Livestream;
import com.jcertpre.model.User;
import com.jcertpre.services.CourseService;
import com.jcertpre.services.InstructorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.jcertpre.model.User.Role.INSTRUCTOR;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private InstructorService instructorService;

    // ====================== BÀI GIẢNG ==========================

    @PostMapping("/courses/{courseId}/add-lesson")
    public String addLesson(@PathVariable Long courseId,
                            @ModelAttribute Lesson lesson,
                            @RequestParam(value = "attachments", required = false) MultipartFile[] files,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        User instructor = (User) session.getAttribute("loggedInUser");
        if (instructor == null || instructor.getRole() != INSTRUCTOR) {
            return "redirect:/login";
        }

        Course course = courseService.findById(courseId);
        if (course == null || !course.getInstructor().getId().equals(instructor.getId())) {
            redirectAttributes.addFlashAttribute("error", "Không có quyền thêm bài giảng.");
            return "redirect:/instructor/courses";
        }

        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    try {
                        String filename = file.getOriginalFilename();
                        Path filePath = Paths.get("uploads/lessons", filename);
                        Files.createDirectories(filePath.getParent());
                        file.transferTo(filePath.toFile());
                        lesson.setAttachmentPath(filePath.toString());
                    } catch (IOException e) {
                        redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu file đính kèm.");
                        return "redirect:/instructor/courses/" + courseId;
                    }
                }
            }
        }

        lesson.setCourse(course);
        instructorService.addLessonToCourse(courseId, lesson, instructor.getId());
        redirectAttributes.addFlashAttribute("message", "Đã thêm bài giảng thành công.");
        return "redirect:/instructor/courses/" + courseId + "/lessons";
    }

    @GetMapping("/courses/{courseId}/lessons")
    public String viewLessonsByCourse(@PathVariable Long courseId,
                                      HttpSession session,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {

        User instructor = (User) session.getAttribute("loggedInUser");
        if (instructor == null || instructor.getRole() != INSTRUCTOR) {
            return "redirect:/login";
        }

        Course course = courseService.findById(courseId);
        if (course == null || !course.getInstructor().getId().equals(instructor.getId())) {
            redirectAttributes.addFlashAttribute("error", "Không có quyền truy cập khóa học này.");
            return "redirect:/instructor/courses";
        }

        model.addAttribute("courseId", courseId);
        model.addAttribute("courseTitle", course.getTitle());
        model.addAttribute("lessons", instructorService.getLessonsByCourse(courseId));
        return "GiangVien_DanhSachBaiGiang";
    }

    @GetMapping("/lessons/all")
    public String viewAllLessonsGrouped(HttpSession session, Model model) {
        User instructor = (User) session.getAttribute("loggedInUser");
        if (instructor == null || instructor.getRole() != INSTRUCTOR) {
            return "redirect:/login";
        }

        Map<Course, List<Lesson>> lessonsByCourse = instructorService.getAllLessonsGroupedByCourse(instructor.getId());
        model.addAttribute("lessonsByCourse", lessonsByCourse);
        return "GiangVien_TatCaBaiGiang";
    }

    @PostMapping("/lessons/update")
    public String updateLessonFromPopup(@RequestParam Long id,
                                        @RequestParam String title,
                                        @RequestParam(required = false) String videoUrl,
                                        @RequestParam(required = false) String slideUrl,
                                        HttpSession session,
                                        RedirectAttributes redirectAttributes) {

        User instructor = (User) session.getAttribute("loggedInUser");
        if (instructor == null || instructor.getRole() != INSTRUCTOR) {
            return "redirect:/login";
        }

        Lesson lesson = instructorService.findLessonById(id);
        if (lesson == null || !lesson.getCourse().getInstructor().getId().equals(instructor.getId())) {
            redirectAttributes.addFlashAttribute("error", "Không có quyền chỉnh sửa bài giảng.");
            return "redirect:/instructor/courses";
        }

        lesson.setTitle(title);
        lesson.setVideoUrl(videoUrl);
        lesson.setSlideUrl(slideUrl);

        instructorService.saveLesson(lesson);
        redirectAttributes.addFlashAttribute("message", "Đã cập nhật bài giảng thành công.");
        return "redirect:/instructor/courses/" + lesson.getCourse().getId() + "/lessons";
    }

    @GetMapping("/lessons/delete/{lessonId}")
    public String deleteLesson(@PathVariable Long lessonId,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        User instructor = (User) session.getAttribute("loggedInUser");
        if (instructor == null || instructor.getRole() != INSTRUCTOR) {
            return "redirect:/login";
        }

        Lesson lesson = instructorService.findLessonById(lessonId);
        if (lesson == null || !lesson.getCourse().getInstructor().getId().equals(instructor.getId())) {
            redirectAttributes.addFlashAttribute("error", "Không có quyền xóa bài giảng.");
            return "redirect:/instructor/courses";
        }

        Long courseId = lesson.getCourse().getId();
        instructorService.deleteLessonById(lessonId);
        redirectAttributes.addFlashAttribute("message", "Đã xóa bài giảng.");
        return "redirect:/instructor/courses/" + courseId + "/lessons";
    }

    // ====================== LIVESTREAM ==========================

    @GetMapping("/livestream/create")
    public String showCreateLivestreamForm(HttpSession session, Model model) {
        User instructor = (User) session.getAttribute("loggedInUser");
        if (instructor == null || instructor.getRole() != INSTRUCTOR) {
            return "redirect:/login";
        }

        model.addAttribute("livestream", new Livestream());
        return "Giangvien_livestream";
    }

    @PostMapping("/livestream/create")
    public String createLivestream(@ModelAttribute Livestream livestream,
                                   @RequestParam("startDate") String startDate,
                                   @RequestParam("startTime") String startTime,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        User instructor = (User) session.getAttribute("loggedInUser");
        if (instructor == null || instructor.getRole() != INSTRUCTOR) {
            return "redirect:/login";
        }

        try {
            LocalDateTime scheduledAt = LocalDateTime.parse(startDate + "T" + startTime);
            livestream.setScheduledAt(scheduledAt);
            livestream.setInstructor(instructor);

            instructorService.scheduleLivestream(livestream, instructor);

            redirectAttributes.addFlashAttribute("message", "Tạo lịch livestream thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tạo lịch livestream.");
        }

        return "redirect:/instructor/livestream/create";
    }

    // 👉 Hiển thị form chỉnh sửa khóa học
    @GetMapping("/courses/{id}/edit")
    public String showEditCourseForm(@PathVariable Long id,
                                     HttpSession session,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        Course course = courseService.findById(id);

        User instructor = (User) session.getAttribute("loggedInUser");
        if (instructor == null || instructor.getRole() != INSTRUCTOR) {
            return "redirect:/login";
        }

        // Kiểm tra khóa học tồn tại và thuộc về instructor hiện tại
        if (course == null || !course.getInstructor().getId().equals(instructor.getId())) {
            redirectAttributes.addFlashAttribute("error", "Không thể chỉnh sửa khóa học không thuộc quyền sở hữu.");
            return "redirect:/instructor/courses";
        }

        model.addAttribute("course", course);
        return "Giangvien_EditCourse"; // Tên file HTML trong /templates
    }

    @PostMapping("/courses/update")
    public String updateCourse(@ModelAttribute Course course,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        User instructor = (User) session.getAttribute("loggedInUser");
        if (instructor == null || instructor.getRole() != INSTRUCTOR) {
            return "redirect:/login";
        }

        Course existing = courseService.findById(course.getId());

        if (existing == null || !existing.getInstructor().getId().equals(instructor.getId())) {
            redirectAttributes.addFlashAttribute("error", "Không thể cập nhật khóa học.");
            return "redirect:/instructor/courses";
        }

        existing.setTitle(course.getTitle());
        existing.setDescription(course.getDescription());
        existing.setCourseType(course.getCourseType());

        courseService.save(existing);
        redirectAttributes.addFlashAttribute("message", "Cập nhật khóa học thành công!");
        return "redirect:/instructor/courses";
    }

}
