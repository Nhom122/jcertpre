package com.jcertpre.services;

import com.jcertpre.model.*;
import com.jcertpre.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InstructorService {

    @Autowired
    private ICourseRepository courseRepository;

    @Autowired
    private ILessonRepository lessonRepository;

    @Autowired
    private ILivestreamRepository livestreamRepository;

    @Autowired
    private IFeedbackRepository feedbackRepository;

    // ----- Tạo khóa học
    public Course createCourse(Course course, User instructor) {
        course.setInstructor(instructor);
        course.setApproved(false); // chờ admin duyệt
        return courseRepository.save(course);
    }

    // ----- Cập nhật khóa học (tương tự, nếu cần)

    // ----- Lấy danh sách khóa học của giảng viên
    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findAll().stream()
                .filter(c -> c.getInstructor().getId().equals(instructorId))
                .toList();
    }

    // ----- Thêm bài giảng vào khóa học
    public Lesson addLessonToCourse(Long courseId, Lesson lesson, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getInstructor().getId().equals(instructorId)) {
            throw new RuntimeException("No permission to add lesson");
        }

        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }

    // ----- Lấy bài giảng của khóa học
    public List<Lesson> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseId(courseId);
    }

    // ----- Tạo lịch livestream
    public Livestream scheduleLivestream(Livestream livestream, User instructor) {
        livestream.setInstructor(instructor);
        return livestreamRepository.save(livestream);
    }

    // ----- Lấy lịch livestream của giảng viên
    public List<Livestream> getLivestreamsByInstructor(Long instructorId) {
        return livestreamRepository.findByInstructorId(instructorId);
    }

    // ----- Lấy câu hỏi (feedback) từ học viên chờ giảng viên trả lời
    public List<Feedback> getPendingFeedback() {
        return feedbackRepository.findByStatus(Feedback.Status.PENDING);
    }

    // ----- Giảng viên trả lời câu hỏi
    public Feedback respondToFeedback(Long feedbackId, String response) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        feedback.setResponse(response);
        feedback.setStatus(Feedback.Status.RESOLVED);
        return feedbackRepository.save(feedback);
    }
}
