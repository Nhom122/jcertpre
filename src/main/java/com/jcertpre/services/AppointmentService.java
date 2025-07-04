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

    // Lưu hoặc cập nhật lịch
    public void saveAppointment(Appointment appointment) {
        repo.save(appointment);
    }

    // Lấy toàn bộ lịch
    public List<Appointment> getAllAppointments() {
        return repo.findAll();
    }

    // Tìm theo ID
    public Appointment getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // Xóa theo ID
    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    // Lịch đang chờ duyệt
    public List<Appointment> getPendingAppointments() {
        return repo.findByStatus("PENDING");
    }

    // Lịch đã duyệt
    public List<Appointment> getApprovedAppointments() {
        return repo.findByStatus("APPROVED");
    }

    // Lịch bị từ chối
    public List<Appointment> getRejectedAppointments() {
        return repo.findByStatus("REJECTED");
    }

    // Truy xuất linh hoạt theo trạng thái
    public List<Appointment> getAppointmentsByStatus(String status) {
        return repo.findByStatus(status);
    }
}
