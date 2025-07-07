package com.jcertpre.repositories;

import com.jcertpre.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByStatus(String status);
}