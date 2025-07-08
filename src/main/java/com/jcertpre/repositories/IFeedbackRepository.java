package com.jcertpre.repositories;

import com.jcertpre.model.Feedback;
import com.jcertpre.model.Feedback.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface IFeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByStatus(Status status);
    List<Feedback> findByLearner_Id(Long learnerId);
    long countByStatus(Status status);
    List<Feedback> findByStatusOrderBySubmittedAtDesc(Feedback.Status status);

    // ❌ KHÔNG dùng nữa: không xóa feedback
    // void deleteByLearnerId(Long learnerId);

    // ✅ Gỡ liên kết learner khỏi feedback
    @Transactional
    @Modifying
    @Query("UPDATE Feedback f SET f.learner = NULL WHERE f.learner.id = :learnerId")
    void detachLearnerByUserId(@Param("learnerId") Long learnerId);
}

