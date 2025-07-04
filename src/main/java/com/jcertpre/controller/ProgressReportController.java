package com.jcertpre.controller;

import com.jcertpre.model.ExamResult;
import com.jcertpre.model.User;
import com.jcertpre.services.ProgressReportService;
import com.jcertpre.services.UserService;
import com.jcertpre.repositories.IExamResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/learner")
public class ProgressReportController {

    @Autowired
    private UserService userService;

    @Autowired
    private IExamResultRepository examResultRepository;

    @Autowired
    private ProgressReportService progressReportService;

    @GetMapping("/progress")
    public String searchProgress(@RequestParam(value = "name", required = false) String name, Model model) {
        if (name == null || name.trim().isEmpty()) {
            return "progress_search"; // chỉ hiển thị form
        }

        // Tìm học viên theo tên (giản lược: so sánh full name)
        List<User> matchedUsers = userService.getAllUsers().stream()
                .filter(u -> u.getFullName().equalsIgnoreCase(name.trim()))
                .toList();

        if (matchedUsers.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy học viên tên: " + name);
            return "progress_search";
        }

        User learner = matchedUsers.get(0); // chọn người đầu tiên khớp

        // Lấy kết quả thi
        List<ExamResult> results = examResultRepository.findAll().stream()
                .filter(r -> r.getStudentName().equalsIgnoreCase(learner.getFullName()))
                .toList();

        ProgressReportService.ReportDTO report = progressReportService.getProgressReport();

        model.addAttribute("studentName", learner.getFullName());
        model.addAttribute("results", results);
        model.addAttribute("report", report);

        return "progress_search";
    }
}
