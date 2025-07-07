package com.jcertpre.services;

import com.jcertpre.model.*;
import com.jcertpre.repositories.ICourseRepository;
import com.jcertpre.repositories.IFeedbackRepository;
import com.jcertpre.repositories.ILessonRepository;
import com.jcertpre.repositories.ILivestreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    // ========== COURSE ==========

    public Course createCourse(Course course, User instructor) {
        course.setInstructor(instructor);
        course.setApproved(false);
        return courseRepository.save(course);
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findAll().stream()
                .filter(c -> c.getInstructor().getId().equals(instructorId))
                .toList();
    }

    // ========== LESSON ==========

    public Lesson addLessonToCourse(Long courseId, Lesson lesson, Long instructorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getInstructor().getId().equals(instructorId)) {
            throw new RuntimeException("No permission to add lesson");
        }

        lesson.setCourse(course);
        return lessonRepository.save(lesson);
    }

    public List<Lesson> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseId(courseId);
    }

    public List<Lesson> getLessonsByInstructor(Long instructorId) {
        return lessonRepository.findLessonsByInstructorId(instructorId);
    }

    public List<Lesson> getAllLessonsByInstructor(Long instructorId) {
        return lessonRepository.findByCourseInstructorId(instructorId);
    }

    public Map<Course, List<Lesson>> getAllLessonsGroupedByCourse(Long instructorId) {
        List<Course> courses = getCoursesByInstructor(instructorId);
        Map<Course, List<Lesson>> map = new LinkedHashMap<>();
        for (Course course : courses) {
            List<Lesson> lessons = getLessonsByCourse(course.getId());
            map.put(course, lessons);
        }
        return map;
    }

    public Lesson findLessonById(Long id) {
        return lessonRepository.findById(id).orElse(null);
    }

    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public void deleteLessonById(Long id) {
        lessonRepository.deleteById(id);
    }

    // ========== LIVESTREAM ==========

    public Livestream scheduleLivestream(Livestream livestream, User instructor) {
        livestream.setInstructor(instructor);
        return livestreamRepository.save(livestream);
    }

    public List<Livestream> getLivestreamsByInstructor(Long instructorId) {
        return livestreamRepository.findByInstructorId(instructorId);
    }

    // ========== FEEDBACK ==========

    public List<Feedback> getPendingFeedback() {
        return feedbackRepository.findByStatus(Feedback.Status.PENDING);
    }

    public Feedback respondToFeedback(Long feedbackId, String response) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        feedback.setResponse(response);
        feedback.setStatus(Feedback.Status.RESOLVED);
        return feedbackRepository.save(feedback);
    }
}