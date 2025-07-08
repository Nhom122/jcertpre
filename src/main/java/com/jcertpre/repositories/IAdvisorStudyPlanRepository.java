package com.jcertpre.repositories;

import com.jcertpre.model.AdvisorStudyPlan;
import com.jcertpre.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IAdvisorStudyPlanRepository extends JpaRepository<AdvisorStudyPlan, Long> {
    List<AdvisorStudyPlan> findByLearner(User learner);
    List<AdvisorStudyPlan> findByAdvisor(User advisor);

    @Modifying
    @Transactional
    @Query("DELETE FROM ConsultationSchedule c WHERE c.learner.id = :userId OR c.advisor.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

}
