package com.jcertpre.repositories;

import com.jcertpre.model.Lesson;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILessonRepository extends CrudRepository<Lesson, Long> {

    // 🔍 Lấy danh sách bài giảng theo ID khóa học
    List<Lesson> findByCourseId(Long courseId);

    // 🔍 Lấy tất cả bài giảng của một giảng viên (qua course.instructor)
    @Query("SELECT l FROM Lesson l WHERE l.course.instructor.id = :instructorId")
    List<Lesson> findLessonsByInstructorId(Long instructorId);

    List<Lesson> findByCourseInstructorId(Long instructorId);

}
