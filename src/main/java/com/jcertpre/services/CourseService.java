package com.jcertpre.services;

import com.jcertpre.model.Course;
import com.jcertpre.repositories.ICourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final ICourseRepository courseRepository;

    public CourseService(ICourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course approveCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setApproved(true);
        return courseRepository.save(course);
    }

    public List<Course> getPendingCourses() {
        return courseRepository.findByApproved(false);
    }
}
