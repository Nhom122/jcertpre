package com.jcertpre.controller;

import com.jcertpre.model.*;
import com.jcertpre.services.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructor")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    // Tạo khóa học mới
    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course, @RequestParam Long instructorId) {
        // Lấy user từ instructorId (thực tế nên xác thực token rồi lấy user)
        User instructor = new User();
        instructor.setId(instructorId);
        instructor.setRole(User.Role.INSTRUCTOR);
        Course created = instructorService.createCourse(course, instructor);
        return ResponseEntity.ok(created);
    }

    // Lấy danh sách khóa học đã tạo
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getCourses(@RequestParam Long instructorId) {
        List<Course> courses = instructorService.getCoursesByInstructor(instructorId);
        return ResponseEntity.ok(courses);
    }

    // Thêm bài giảng
    @PostMapping("/courses/{courseId}/lessons")
    public ResponseEntity<Lesson> addLesson(@PathVariable Long courseId,
                                            @RequestBody Lesson lesson,
                                            @RequestParam Long instructorId) {
        Lesson created = instructorService.addLessonToCourse(courseId, lesson, instructorId);
        return ResponseEntity.ok(created);
    }

    // Lấy danh sách bài giảng
    @GetMapping("/courses/{courseId}/lessons")
    public ResponseEntity<List<Lesson>> getLessons(@PathVariable Long courseId) {
        List<Lesson> lessons = instructorService.getLessonsByCourse(courseId);
        return ResponseEntity.ok(lessons);
    }

    // Lên lịch livestream
    @PostMapping("/livestreams")
    public ResponseEntity<Livestream> scheduleLivestream(@RequestBody Livestream livestream,
                                                         @RequestParam Long instructorId) {
        User instructor = new User();
        instructor.setId(instructorId);
        instructor.setRole(User.Role.INSTRUCTOR);
        Livestream scheduled = instructorService.scheduleLivestream(livestream, instructor);
        return ResponseEntity.ok(scheduled);
    }

    // Lấy lịch livestream
    @GetMapping("/livestreams")
    public ResponseEntity<List<Livestream>> getLivestreams(@RequestParam Long instructorId) {
        List<Livestream> livestreams = instructorService.getLivestreamsByInstructor(instructorId);
        return ResponseEntity.ok(livestreams);
    }

    // Xem câu hỏi học viên (feedback chưa trả lời)
    @GetMapping("/feedbacks/pending")
    public ResponseEntity<List<Feedback>> getPendingFeedbacks() {
        List<Feedback> feedbacks = instructorService.getPendingFeedback();
        return ResponseEntity.ok(feedbacks);
    }

    // Trả lời câu hỏi học viên
    @PostMapping("/feedbacks/{id}/respond")
    public ResponseEntity<Feedback> respondToFeedback(@PathVariable Long id, @RequestBody String response) {
        Feedback updated = instructorService.respondToFeedback(id, response);
        return ResponseEntity.ok(updated);
    }
}
