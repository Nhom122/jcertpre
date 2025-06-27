package com.jcertpre.controller;

import com.jcertpre.model.Question;
import com.jcertpre.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService service;

    @GetMapping("/ask")
    public String showForm(Model model) {
        model.addAttribute("question", new Question());
        return "ask_question";
    }

    @PostMapping("/ask")
    public String submit(@ModelAttribute Question question) {
        service.save(question);
        return "redirect:/ask/success";
    }

    @GetMapping("/ask/success")
    public String success() {
        return "ask_success";
    }

    // Trang dành cho giảng viên
    @GetMapping("/questions")
    public String viewAll(Model model) {
        model.addAttribute("questions", service.getAll());
        return "question_list";
    }
}