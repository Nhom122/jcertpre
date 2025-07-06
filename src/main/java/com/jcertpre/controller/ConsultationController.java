package com.jcertpre.controller;

import com.jcertpre.model.ConsultationSchedule;
import com.jcertpre.model.User;
import com.jcertpre.repositories.IConsultationScheduleRepository;
import com.jcertpre.services.ConsultationScheduleService;
import com.jcertpre.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ConsultationController {

    @Autowired
    private IConsultationScheduleRepository consultationScheduleRepository;

    @Autowired
    private UserService userService;

    /*// 1. Học viên đặt lịch tư vấn
    @GetMapping("/consultation")
    public String showForm(Model model) {
        model.addAttribute("consultation", new ConsultationSchedule());
        return "consultation_form";
    }

    @PostMapping("/consultation")
    public String submitForm(@ModelAttribute ConsultationSchedule consultation,
                             HttpSession session) {
        consultation.setStatus("PENDING");

        // Lấy người dùng từ session
        User learner = (User) session.getAttribute("loggedInUser");
        consultation.setLearner(learner);

        // Set thời gian mẫu nếu cần kiểm tra (nên sửa lại trong view)
        if (consultation.getStartTime() == null) {
            consultation.setStartTime(LocalDateTime.now());
            consultation.setEndTime(LocalDateTime.now().plusHours(1));
        }

        service.save(consultation);
        return "redirect:/consultation/success";
    }*/

    @GetMapping("/consultation/success")
    public String success() {
        return "consultation_success";
    }

    // 2. Cố vấn xem lịch đã duyệt
    @GetMapping("/consultations/approved")
    public String viewApproved(Model model) {
        List<ConsultationSchedule> list = consultationScheduleRepository.findByStatus("APPROVED");
        model.addAttribute("consultations", list);
        return "consultation_approved";
    }

    // 3. ADMIN xem tất cả yêu cầu
    @GetMapping("/admin/consultations")
    public String viewAll(Model model) {
        List<ConsultationSchedule> list = consultationScheduleRepository.findAll();
        model.addAttribute("consultations", list);
        return "Admin_duyetcovan";
    }

    // ✅ Duyệt lịch
    @PostMapping("/consultations/approve/{id}")
    public String approve(@PathVariable Long id, RedirectAttributes redirect) {
        consultationScheduleRepository.findById(id).ifPresent(c -> {
            c.setStatus("APPROVED");
            consultationScheduleRepository.save(c);
        });
        redirect.addFlashAttribute("message", "✅ Lịch đã được duyệt.");
        return "redirect:/admin/consultations";
    }


    // ❌ Từ chối lịch
    @PostMapping("/consultations/reject/{id}")
    public String reject(@PathVariable Long id,
                         @RequestParam("reason") String reason,
                         RedirectAttributes redirect) {
        consultationScheduleRepository.findById(id).ifPresent(c -> {
            c.setStatus("REJECTED");
            consultationScheduleRepository.save(c);
        });
        redirect.addFlashAttribute("message", "❌ Lịch đã bị từ chối.");
        return "redirect:/admin/consultations";
    }
}
