package com.example.demo.controller;

import com.example.demo.model.Appointment;
import com.example.demo.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentService service;

    @GetMapping("/appointment")
    public String showForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        return "appointment";
    }

    @PostMapping("/appointment")
    public String submitForm(@ModelAttribute Appointment appointment) {
        service.saveAppointment(appointment);
        return "redirect:/appointment/success";
    }

    @GetMapping("/appointment/success")
    public String success() {
        return "appointment_success";
    }

    @GetMapping("/appointments")
    public String viewAllAppointments(Model model) {
        model.addAttribute("appointments", service.getAllAppointments());
        model.addAttribute("appointment", new Appointment());
        return "appointments";
    }

    @GetMapping("/appointments/edit/{id}")
    public String editAppointment(@PathVariable("id") Long id, Model model) {
        Appointment appointment = service.getById(id);
        model.addAttribute("appointment", appointment);
        model.addAttribute("appointments", service.getAllAppointments());
        return "appointments";
    }

    // ✅ Giữ DUY NHẤT phương thức update này
    @PostMapping("/appointments/update/{id}")
    public String updateAppointment(@PathVariable("id") Long id,
                                    @ModelAttribute Appointment appointment,
                                    RedirectAttributes redirectAttrs) {
        Appointment existing = service.getById(id);
        if (existing != null) {
            existing.setStudentName(appointment.getStudentName());
            existing.setEmail(appointment.getEmail());
            existing.setDate(appointment.getDate());
            existing.setTimeSlot(appointment.getTimeSlot());
            existing.setReason(appointment.getReason());
            service.saveAppointment(existing);
        }
        redirectAttrs.addFlashAttribute("message", "Cập nhật thành công!");
        return "redirect:/appointments";
    }

    @PostMapping("/appointment/delete/{id}")
    public String deleteAppointment(@PathVariable("id") Long id) {
        service.deleteById(id);
        return "redirect:/appointments";
    }
}
