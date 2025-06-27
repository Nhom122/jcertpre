package com.jcertpre.services;

import com.jcertpre.model.AdvisorStudyPlan;
import com.jcertpre.model.User;
import com.jcertpre.repositories.AdvisorStudyPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdvisorStudyPlanService {
    private final AdvisorStudyPlanRepository repo;

    public AdvisorStudyPlanService(AdvisorStudyPlanRepository repo) {
        this.repo = repo;
    }

    public AdvisorStudyPlan create(AdvisorStudyPlan plan) {
        return repo.save(plan);
    }

    public List<AdvisorStudyPlan> getByAdvisor(User advisor) {
        return repo.findByAdvisor(advisor);
    }

    public List<AdvisorStudyPlan> getByLearner(User learner) {
        return repo.findByLearner(learner);
    }

}
