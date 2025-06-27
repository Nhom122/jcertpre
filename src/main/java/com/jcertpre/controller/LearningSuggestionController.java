package com.jcertpre.controller;

import com.jcertpre.model.ExamResult;
import com.jcertpre.services.LearningSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class LearningSuggestionController {

    @Autowired
    private LearningSuggestionService suggestionService;

    @GetMapping("/suggestions")
    public String showSuggestions(Model model) {
        Map<ExamResult, String> suggestions = suggestionService.getSuggestions();
        model.addAttribute("suggestions", suggestions);
        return "suggestion"; // File Thymeleaf HTML
    }
}