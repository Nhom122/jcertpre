package com.jcertpre.controller;

import com.jcertpre.model.QuizItem;
import com.jcertpre.services.QuizItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/instructor/quizitem")
public class QuizItemController {

    @Autowired
    private QuizItemService quizItemService;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("quizItems", quizItemService.getAllQuizItems());
        return "Quiz_danhsachcauhoi";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("quizItem", new QuizItem());
        return "Quiz_taocauhoi";
    }

    @PostMapping("/create")
    public String createSubmit(@RequestParam String text,
                               @RequestParam("options") List<String> options,
                               @RequestParam String correctAnswer) {
        QuizItem quizItem = new QuizItem();
        quizItem.setText(text);
        quizItem.setOptions(options);
        quizItem.setCorrectAnswer(correctAnswer.toUpperCase());
        quizItemService.save(quizItem);
        return "redirect:/instructor/quizitem/list";
    }
}