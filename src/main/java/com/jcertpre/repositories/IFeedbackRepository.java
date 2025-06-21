package com.jcertpre.repositories;

import com.jcertpre.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

import java.util.List;

@Repository
public interface IFeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByStatus(Feedback.Status status);
    List<Feedback> findByLearner_Id(Long learnerId);
}