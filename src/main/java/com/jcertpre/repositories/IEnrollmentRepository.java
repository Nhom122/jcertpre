package com.jcertpre.repositories;

import com.jcertpre.model.Enrollment;
import com.jcertpre.model.Course;
import com.jcertpre.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IEnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query("SELECT e.course FROM Enrollment e WHERE e.learner.id = :learnerId")
    List<Course> findCoursesByLearnerId(@Param("learnerId") Long learnerId);

    boolean existsByLearnerAndCourse(User learner, Course course);

    List<Enrollment> findByLearnerId(Long learnerId);

    boolean existsByLearnerIdAndCourseId(Long learnerId, Long courseId);
}
