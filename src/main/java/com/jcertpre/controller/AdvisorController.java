package com.jcertpre.controller;

import com.jcertpre.model.AdvisorStudyPlan;
import com.jcertpre.model.ConsultationSchedule;
import com.jcertpre.model.ExamResult;
import com.jcertpre.model.User;
import com.jcertpre.repositories.IAdvisorStudyPlanRepository;
import com.jcertpre.repositories.IConsultationScheduleRepository;
import com.jcertpre.repositories.IExamResultRepository;
import com.jcertpre.repositories.IUserRepository;
import com.jcertpre.services.AdvisorStudyPlanService;
import com.jcertpre.services.ConsultationScheduleService;
import com.jcertpre.services.ProgressReportService;
import com.jcertpre.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Autowired
    private UserService userService;

    @Autowired
    private IExamResultRepository examResultRepository;

    @Autowired
    private ProgressReportService progressReportService;

    @Autowired
    private IAdvisorStudyPlanRepository advisorStudyPlanRepository;

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

    @GetMapping("/advisor/dashboard/createstudyplan")
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

    @GetMapping("/advisor/dashboard/liststudyplan")
    public String showStudyplan(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || user.getRole() != ADVISOR) {
            return "login";
        }

        List<AdvisorStudyPlan> plans = advisorStudyPlanRepository.findByAdvisor(user);

        if (plans == null) {
            plans = new ArrayList<>();
        }

        model.addAttribute("plans", plans);

        return "CoVanListStudyPlan";
    }

    @PostMapping("/advisor/dashboard/studyplanapi")
    public String createStudyPlan(
            @RequestParam("learnerId") Long learnerId,
            @RequestParam("goal") String goal,
            @RequestParam("strategy") String strategy,
            @RequestParam(value = "progressNote", required = false) String progressNote,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        // Lấy thông tin học viên và cố vấn
        User learner = userRepository.findById(learnerId).orElse(null);
        User advisor = (User) session.getAttribute("loggedInUser");

        // Kiểm tra người dùng hợp lệ
        if (learner == null || advisor == null) {
            model.addAttribute("errorMessage", "Không tìm thấy học viên hoặc người tư vấn. Vui lòng kiểm tra lại hoặc đăng nhập lại.");
            return "CoVanKeHoachhoctap";
        } else if (learner.getRole() != LEARNER || advisor.getRole() != ADVISOR) {
            model.addAttribute("errorMessage", "Không hợp lệ. Chỉ học viên và cố vấn mới được thao tác.");
            return "CoVanKeHoachhoctap";
        }

        // Tạo đối tượng AdvisorStudyPlan
        AdvisorStudyPlan studyPlan = new AdvisorStudyPlan(
                goal,
                strategy,
                progressNote,
                learner,
                advisor
        );

        // Lưu kế hoạch vào DB
        advisorStudyPlanRepository.save(studyPlan);

        // Gửi thông báo thành công
        redirectAttributes.addFlashAttribute("message", "Đã gửi đề xuất kế hoạch học tập thành công.");
        return "redirect:/advisor/dashboard/liststudyplan"; // Chuyển về danh sách kế hoạch
    }

    @GetMapping("/advisor/dashboard/progress")
    public String searchProgress(@RequestParam(value = "id", required = false) Long learnerId, Model model,HttpSession session ) {
        User user = (User) session.getAttribute("loggedInUser");

        if (user == null || user.getRole() != ADVISOR) {
            return "login";
        }

        if (learnerId == null) {
            return "Covan_capnhattiendo"; // chỉ hiển thị form nếu chưa nhập ID
        }

        User learner = userRepository.findById(learnerId).orElse(null);

        // Tìm học viên theo ID
        if (learner == null) {
            model.addAttribute("errorMessage", "Không tìm thấy học viên hoặc người tư vấn. Vui lòng kiểm tra lại hoặc đăng nhập lại.");
            return "Covan_capnhattiendo";
        } else if (learner.getRole() != LEARNER) {
            model.addAttribute("errorMessage", "Không hợp lệ. Chỉ học viên và cố vấn mới được thao tác.");
            return "Covan_capnhattiendo";
        }

        // Lấy kết quả thi theo tên học viên
        List<ExamResult> results = examResultRepository.findAll().stream()
                .filter(r -> r.getStudentName().equalsIgnoreCase(learner.getFullName()))
                .toList();

        // Tạo báo cáo tiến độ (giả định)
        ProgressReportService.ReportDTO report = progressReportService.getProgressReport();

        // Truyền dữ liệu sang view
        model.addAttribute("studentName", learner.getFullName());
        model.addAttribute("results", results);
        model.addAttribute("report", report);

        return "Covan_capnhattiendo";
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
