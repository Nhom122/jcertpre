package com.jcertpre.controller;

import com.jcertpre.model.ExamResult;
import com.jcertpre.services.ExamResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ExamResultController {

    @Autowired
    private ExamResultService service;

    @GetMapping("/results")
    public String showResults(Model model) {
        List<ExamResult> results = service.getAllResults();
        model.addAttribute("results", results);
        return "result"; // result.html
    }
}
