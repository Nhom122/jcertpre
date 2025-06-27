package com.jcertpre.services;

import com.jcertpre.model.Appointment;
import com.jcertpre.repositories.IAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private IAppointmentRepository repo;

    public void saveAppointment(Appointment appointment) {
        repo.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    public Appointment findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public Appointment getById(Long id) {
        return repo.findById(id).orElse(null);
    }

}