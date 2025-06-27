package com.jcertpre.services;

import com.jcertpre.model.Course;
import com.jcertpre.repositories.ICourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private final ICourseRepository courseRepository;

    public CourseService(ICourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    //Lay tat ca danh sach khoa hoc
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
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
