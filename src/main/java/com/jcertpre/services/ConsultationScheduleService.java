package com.jcertpre.services;

import com.jcertpre.model.ConsultationSchedule;
import com.jcertpre.model.User;
import com.jcertpre.repositories.IConsultationScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultationScheduleService {
    private final IConsultationScheduleRepository repo;

    public ConsultationScheduleService(IConsultationScheduleRepository repo) {
        this.repo = repo;
    }

    public ConsultationSchedule findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));
    }

    public List<ConsultationSchedule> findByStatus(String status) {
        return repo.findByStatus(status);
    }

    public List<ConsultationSchedule> findAll() {
        return repo.findAll();
    }

    public ConsultationSchedule create(ConsultationSchedule cs) {
        return repo.save(cs);
    }

    public List<ConsultationSchedule> getByAdvisor(User advisor) {
        return repo.findByAdvisor(advisor);
    }

    public List<ConsultationSchedule> getPendingByAdvisor(User advisor) {
        return repo.findByAdvisorAndStatus(advisor, "PENDING");
    }

    public List<ConsultationSchedule> getByLearner(User learner) {
        return repo.findByLearner(learner);
    }
}
