package com.jcertpre.services;

import com.jcertpre.model.AdvisorStudyPlan;
import com.jcertpre.model.User;
import com.jcertpre.repositories.IAdvisorStudyPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdvisorStudyPlanService {
    private final IAdvisorStudyPlanRepository repo;

    public AdvisorStudyPlanService(IAdvisorStudyPlanRepository repo) {
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
