package com.jcertpre.repositories;

import com.jcertpre.model.ConsultationSchedule;
import com.jcertpre.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IConsultationScheduleRepository extends JpaRepository<ConsultationSchedule, Long> {
    List<ConsultationSchedule> findByAdvisor(User advisor);
    List<ConsultationSchedule> findByLearner(User learner);
    List<ConsultationSchedule> findByAdvisorAndStatus(User advisor, String status);
    List<ConsultationSchedule> findByStatus(String status);

    @Modifying
    @Transactional
    @Query("DELETE FROM ConsultationSchedule c WHERE c.learner.id = :userId OR c.advisor.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}