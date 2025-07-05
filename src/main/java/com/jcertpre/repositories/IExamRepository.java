package com.jcertpre.repositories;

import com.jcertpre.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IExamRepository extends JpaRepository<Exam, Long> {
}
