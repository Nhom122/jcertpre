package com.jcertpre.services;

import com.jcertpre.model.Course;
import com.jcertpre.model.User;
import com.jcertpre.repositories.ICourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final ICourseRepository courseRepository;

    @Autowired
    public CourseService(ICourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
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

    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }
}
