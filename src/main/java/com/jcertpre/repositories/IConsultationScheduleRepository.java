package com.jcertpre.repositories;

import com.jcertpre.model.ConsultationSchedule;
import com.jcertpre.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IConsultationScheduleRepository extends JpaRepository<ConsultationSchedule, Long> {
    List<ConsultationSchedule> findByAdvisor(User advisor);
    List<ConsultationSchedule> findByLearner(User learner);
}