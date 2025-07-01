package com.jcertpre.controller;

import com.jcertpre.model.Course;
import com.jcertpre.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/courses")
    public String showCourses(Model model) {
        List<Course> courseList = courseService.getAllCourses();
        model.addAttribute("courses", courseList);
        return "courses";
    }
}