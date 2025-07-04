package com.jcertpre.repositories;

import com.jcertpre.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IQuestionRepository extends JpaRepository<Question, Long> {
}