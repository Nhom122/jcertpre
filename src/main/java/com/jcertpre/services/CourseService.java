package com.jcertpre.services;

import com.jcertpre.model.Course;
import com.jcertpre.model.User;
import com.jcertpre.repositories.ICourseRepository;
import com.jcertpre.repositories.IEnrollmentRepository;
import com.jcertpre.repositories.ILessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private final ICourseRepository courseRepository;
    private final IEnrollmentRepository enrollmentRepository;
    private final ILessonRepository lessonRepository;

    @Autowired
    public CourseService(ICourseRepository courseRepository, IEnrollmentRepository enrollmentRepository, ILessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.lessonRepository = lessonRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getApprovedCourses() {
        return courseRepository.findByApproved(true);
    }

    public List<Course> getPendingCourses() {
        return courseRepository.findByApproved(false);
    }

    public Course approveCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setApproved(true);
        course.setRejectionReason(null); // reset nếu trước đó bị từ chối
        return courseRepository.save(course);
    }

    public Course rejectCourse(Long courseId, String reason) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setApproved(false);
        course.setRejectionReason(reason);
        return courseRepository.save(course);
    }

    public long countPendingCourses() {
        return courseRepository.countByApproved(false);
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    // ✅ Bổ sung đúng: dùng repository để truy vấn
    public List<Course> getCoursesByInstructor(User instructor) {
        return courseRepository.findByInstructor(instructor);
    }

    public Course findById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteById(Long id) {
        lessonRepository.deleteByCourseId(id);       // Xóa bài học trước
        enrollmentRepository.deleteByCourseId(id);   // Rồi mới xóa học viên
        courseRepository.deleteById(id);             // Cuối cùng xóa khóa học
    }

}
