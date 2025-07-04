package com.jcertpre.repositories;

import com.jcertpre.model.Course;
import com.jcertpre.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByApproved(boolean approved);

    long countByApproved(boolean approved);

    List<Course> findByInstructor(User instructor);
}
