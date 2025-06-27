package com.jcertpre.repositories;

import com.jcertpre.model.AdvisorStudyPlan;
import com.jcertpre.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAdvisorStudyPlanRepository extends JpaRepository<AdvisorStudyPlan, Long> {
    List<AdvisorStudyPlan> findByLearner(User learner);
    List<AdvisorStudyPlan> findByAdvisor(User advisor);
}
