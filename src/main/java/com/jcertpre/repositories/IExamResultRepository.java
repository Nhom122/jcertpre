package com.jcertpre.repositories;

import com.jcertpre.model.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findByStudentName(String studentName);
}