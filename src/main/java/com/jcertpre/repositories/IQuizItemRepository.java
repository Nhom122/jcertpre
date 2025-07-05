package com.jcertpre.repositories;

import com.jcertpre.model.QuizItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuizItemRepository extends JpaRepository<QuizItem, Long> {
}
