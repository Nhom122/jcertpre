package com.jcertpre.controller;

import com.jcertpre.model.Appointment;
import com.jcertpre.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
@Controller
public class AppointmentController {

    @Autowired
    private AppointmentService service;


    // 1. Học viên gửi yêu cầu tư vấn
    @GetMapping("/appointment")
    public String showForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        return "appointment";
    }

    @PostMapping("/appointment")
    public String submitForm(@ModelAttribute Appointment appointment) {
        appointment.setStatus("PENDING");
        service.saveAppointment(appointment);
        return "redirect:/appointment/success";
    }

    @GetMapping("/appointment/success")
    public String success() {
        return "appointment_success";
    }

    // 2. Cố vấn xem lịch đã được duyệt
    @GetMapping("/appointments/approved")
    public String viewApprovedAppointments(Model model) {
        List<Appointment> approved = service.getAppointmentsByStatus("APPROVED");
        model.addAttribute("appointments", approved);
        return "approved_appointments";
    }

    // 3. ADMIN xử lý tất cả lịch trên một trang duy nhất
    @GetMapping("/appointments/admin")
    public String adminViewAllAppointments(Model model) {
        List<Appointment> all = service.getAllAppointments();
        model.addAttribute("appointments", all);
        return "Admin_duyetcovan";
    }

    // Duyệt lịch
    @PostMapping("/appointments/approve/{id}")
    public String approveAppointment(@PathVariable Long id, RedirectAttributes redirect) {
        Appointment ap = service.getById(id);
        if (ap != null) {
            ap.setStatus("APPROVED");
            ap.setRejectionReason(null);
            service.saveAppointment(ap);
        }
        redirect.addFlashAttribute("message", "✅ Lịch đã được duyệt.");
        return "redirect:/appointments/admin";
    }

    // Từ chối lịch
    @PostMapping("/appointments/reject/{id}")
    public String rejectAppointment(@PathVariable Long id,
                                    @RequestParam("reason") String reason,
                                    RedirectAttributes redirect) {
        Appointment ap = service.getById(id);
        if (ap != null) {
            ap.setStatus("REJECTED");
            ap.setRejectionReason(reason);
            service.saveAppointment(ap);
        }
        redirect.addFlashAttribute("message", "❌ Lịch đã bị từ chối.");
        return "redirect:/appointments/admin";
    }
}
