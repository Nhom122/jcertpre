package com.jcertpre.controller;

import com.jcertpre.services.ProgressReportService;
import com.jcertpre.services.ProgressReportService.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProgressReportController {

    @Autowired
    private ProgressReportService reportService;

    @GetMapping("/report")
    public String viewProgressReport(Model model) {
        ReportDTO report = reportService.getProgressReport();
        model.addAttribute("report", report);
        return "report";
    }
}