package com.jcertpre.controller;

import com.jcertpre.model.AdvisorStudyPlan;
import com.jcertpre.model.ConsultationSchedule;
import com.jcertpre.model.User;
import com.jcertpre.repositories.IConsultationScheduleRepository;
import com.jcertpre.repositories.IUserRepository;
import com.jcertpre.services.AdvisorStudyPlanService;
import com.jcertpre.services.ConsultationScheduleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.jcertpre.model.User.Role.ADVISOR;
import static com.jcertpre.model.User.Role.LEARNER;

@Controller
public class AdvisorController {
    @Autowired
    private ConsultationScheduleService scheduleService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IConsultationScheduleRepository consultationScheduleRepository;

    @GetMapping("/advisor/dashboard/schedule")
    public String showScheduleForm(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "login";
        }

        if (ADVISOR != user.getRole()) {
            return "login";
        }

        return "CoVanLichTuVan";
    }

    @PostMapping("/advisor/schedule")
    public String createSchedule(
            @RequestParam("learnerId") Long learnerId,
            @RequestParam("consultDate") String consultDate,
            @RequestParam("timeSlot") String timeSlot,
            @RequestParam(value = "note", required = false) String topic,
            @RequestParam("status") String status,
            HttpSession session,
            Model model
    ) {


        // Tách giờ bắt đầu và kết thúc từ timeSlot (ví dụ: "08:00-09:00")
        String[] timeParts = timeSlot.split("-");
        String startTimeStr = consultDate + "T" + timeParts[0];
        String endTimeStr = consultDate + "T" + timeParts[1];

        LocalDateTime startTime = LocalDateTime.parse(startTimeStr);
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr);

        // Tìm người học và cố vấn từ DB
        User learner = userRepository.findById(learnerId).orElse(null);
        User advisor = (User) session.getAttribute("loggedInUser");

        if (learner == null || advisor == null) {
            model.addAttribute("errorMessage", "Không tìm thấy học viên hoặc người tư vấn. Vui lòng kiểm tra lại mã số học viên hoặc đăng nhập lại.");
            return "CoVanLichTuVan";
        }
        else if (learner.getRole() != LEARNER || advisor.getRole() != ADVISOR) {
            model.addAttribute("errorMessage", "Không tìm thấy học viên hoặc người tư vấn. Vui lòng kiểm tra lại mã số học viên hoặc đăng nhập lại.");
            return "CoVanLichTuVan";
        }

        // Tạo lịch tư vấn
        ConsultationSchedule schedule = new ConsultationSchedule(
                status,
                learner,
                advisor,
                topic,
                endTime,
                startTime
        );


        consultationScheduleRepository.save(schedule);

        return "redirect:/advisor/dashboard"; // hoặc thông báo thành công
    }

    @GetMapping("/advisor/dashboard/detail")
    public String showDetailSchedule(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "login";
        }

        if (ADVISOR != user.getRole()) {
            return "login";
        }

        List<ConsultationSchedule> pendingSchedules = scheduleService.getPendingByAdvisor(user);
        model.addAttribute("pendingConsultations", pendingSchedules.size());
        model.addAttribute("pendingList", pendingSchedules);
        return "CoVanTuvancanhan";
    }

    @GetMapping("/advisor/dashboard/studyplan")
    public String showStudyplanForm(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "login";
        }

        if (ADVISOR != user.getRole()) {
            return "login";
        }

        return "CoVanKeHoachhoctap";
    }









    /*private final AdvisorStudyPlanService studyPlanService;
    private final ConsultationScheduleService consultationService;

    public AdvisorController(AdvisorStudyPlanService studyPlanService, ConsultationScheduleService consultationService, ConsultationScheduleService scheduleService, IUserRepository userRepository) {
        this.studyPlanService = studyPlanService;
        this.consultationService = consultationService;
    }

    @PostMapping("/study-plan")
    public AdvisorStudyPlan createPlan(@RequestBody AdvisorStudyPlan plan) {
        return studyPlanService.create(plan);
    }

    @PostMapping("/consultation")
    public ConsultationSchedule createConsultation(@RequestBody ConsultationSchedule cs) {
        return consultationService.create(cs);
    }

    @PutMapping("/consultation/{id}/approve")
    public void approveConsultation(@PathVariable Long id) {
        consultationService.approve(id);
    }
*/
}
