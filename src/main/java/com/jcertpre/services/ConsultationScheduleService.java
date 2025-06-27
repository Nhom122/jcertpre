package com.jcertpre.services;

import com.jcertpre.model.ConsultationSchedule;
import com.jcertpre.model.User;
import com.jcertpre.repositories.IConsultationScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultationScheduleService {
    private final IConsultationScheduleRepository repo;

    public ConsultationScheduleService(IConsultationScheduleRepository repo) {
        this.repo = repo;
    }

    public ConsultationSchedule create(ConsultationSchedule cs) {
        return repo.save(cs);
    }

    public List<ConsultationSchedule> getByAdvisor(User advisor) {
        return repo.findByAdvisor(advisor);
    }

    public List<ConsultationSchedule> getByLearner(User learner) {
        return repo.findByLearner(learner);
    }

    public void approve(Long id) {
        ConsultationSchedule cs = repo.findById(id).orElseThrow();
        cs.setStatus("APPROVED");
        repo.save(cs);
    }

}
