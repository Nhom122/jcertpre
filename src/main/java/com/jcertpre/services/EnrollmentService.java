package com.jcertpre.services;

import com.jcertpre.model.Course;
import com.jcertpre.model.Enrollment;
import com.jcertpre.model.User;
import com.jcertpre.repositories.IEnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private IEnrollmentRepository enrollmentRepository;

    // Lấy danh sách khóa học học viên đã đăng ký
    public List<Course> getCoursesByLearner(Long learnerId) {
        return enrollmentRepository.findCoursesByLearnerId(learnerId);
    }

    // Đăng ký khóa học
    public void enroll(User learner, Course course) {
        if (!enrollmentRepository.existsByLearnerIdAndCourseId(learner.getId(), course.getId())) {
            Enrollment enrollment = new Enrollment(learner, course, 0.0, LocalDateTime.now());
            enrollmentRepository.save(enrollment);
        }
    }

    // Lấy danh sách đối tượng Enrollment của học viên
    public List<Enrollment> getEnrollmentsByLearner(Long learnerId) {
        return enrollmentRepository.findByLearnerId(learnerId);
    }

    public void enrollLearnerToCourse(User learner, Course course) {
        // Kiểm tra học viên đã đăng ký chưa (tránh trùng lặp)
        boolean alreadyEnrolled = enrollmentRepository.existsByLearnerAndCourse(learner, course);
        if (alreadyEnrolled) return;

        Enrollment enrollment = new Enrollment();
        enrollment.setLearner(learner);
        enrollment.setCourse(course);
        enrollment.setProgress(0.0);
        enrollment.setEnrolledAt(LocalDateTime.now());

        enrollmentRepository.save(enrollment);
    }
}
