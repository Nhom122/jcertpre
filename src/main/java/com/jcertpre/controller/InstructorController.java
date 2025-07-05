package com.jcertpre.controller;

import com.jcertpre.model.Course;
import com.jcertpre.model.Lesson;
import com.jcertpre.model.Livestream;
import com.jcertpre.model.User;
import com.jcertpre.services.CourseService;
import com.jcertpre.services.InstructorService;
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
                            @SessionAttribute("currentUser") User instructor,
                            RedirectAttributes redirectAttributes) {

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
                                      @SessionAttribute("currentUser") User instructor,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {

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
    public String viewAllLessonsGrouped(@SessionAttribute("currentUser") User instructor, Model model) {
        Map<Course, List<Lesson>> lessonsByCourse = instructorService.getAllLessonsGroupedByCourse(instructor.getId());
        model.addAttribute("lessonsByCourse", lessonsByCourse);
        return "GiangVien_TatCaBaiGiang";
    }

    @PostMapping("/lessons/update")
    public String updateLessonFromPopup(@RequestParam Long id,
                                        @RequestParam String title,
                                        @RequestParam(required = false) String videoUrl,
                                        @RequestParam(required = false) String slideUrl,
                                        @SessionAttribute("currentUser") User instructor,
                                        RedirectAttributes redirectAttributes) {

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
                               @SessionAttribute("currentUser") User instructor,
                               RedirectAttributes redirectAttributes) {

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
    public String showCreateLivestreamForm(Model model) {
        model.addAttribute("livestream", new Livestream());
        return "Giangvien_themlivestream";
    }

    @GetMapping("/livestream/manage")
    public String manageLivestreams(@SessionAttribute("currentUser") User instructor, Model model) {
        List<Livestream> livestreams = instructorService.getLivestreamsByInstructor(instructor.getId());
        model.addAttribute("livestreams", livestreams);
        return "GiangVien_QuanLyLivestream";
    }

    @PostMapping("/livestream/create")
    public String createLivestream(@ModelAttribute Livestream livestream,
                                   @RequestParam("startDate") String startDate,
                                   @RequestParam("startTime") String startTime,
                                   @SessionAttribute("currentUser") User instructor,
                                   RedirectAttributes redirectAttributes) {
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

    // 👉 Cập nhật livestream
    @PostMapping("/livestream/update")
    public String updateLivestream(@RequestParam Long id,
                                   @RequestParam String title,
                                   @RequestParam String scheduledAt,
                                   @RequestParam String meetingLink,
                                   @RequestParam String description,
                                   @SessionAttribute("currentUser") User instructor,
                                   RedirectAttributes redirectAttributes) {
        Livestream stream = instructorService.findLivestreamById(id);
        if (stream == null || !stream.getInstructor().getId().equals(instructor.getId())) {
            redirectAttributes.addFlashAttribute("error", "Không có quyền chỉnh sửa.");
            return "redirect:/instructor/livestream/manage";
        }

        stream.setTitle(title);
        stream.setScheduledAt(LocalDateTime.parse(scheduledAt));
        stream.setMeetingLink(meetingLink);
        stream.setDescription(description);
        instructorService.saveLivestream(stream);

        redirectAttributes.addFlashAttribute("message", "Cập nhật livestream thành công.");
        return "redirect:/instructor/livestream/manage";
    }

    // 👉 Xóa livestream
    @PostMapping("/livestream/delete/{id}")
    public String deleteLivestream(@PathVariable Long id,
                                   @SessionAttribute("currentUser") User instructor,
                                   RedirectAttributes redirectAttributes) {
        Livestream stream = instructorService.findLivestreamById(id);
        if (stream == null || !stream.getInstructor().getId().equals(instructor.getId())) {
            redirectAttributes.addFlashAttribute("error", "Không có quyền xóa.");
            return "redirect:/instructor/livestream/manage";
        }

        instructorService.deleteLivestreamById(id);
        redirectAttributes.addFlashAttribute("message", "Đã xóa livestream.");
        return "redirect:/instructor/livestream/manage";
    }
}
