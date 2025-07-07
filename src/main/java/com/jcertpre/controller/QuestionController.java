package com.jcertpre.controller;

import com.jcertpre.model.Question;
import com.jcertpre.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService service;

    // Trang người học đặt câu hỏi
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
    // Trang quản lý câu hỏi cho giảng viên
    @GetMapping("/questions")
    public String viewAllQuestions(Model model) {
        model.addAttribute("questions", service.getAll());
        return "question_list";
    }

    // Gửi phản hồi
    @PostMapping("/questions/answer")
    public String replyToQuestion(@RequestParam("id") Long id,
                                  @RequestParam("reply") String reply) {
        Question question = service.getById(id);
        if (question != null && (question.getReply() == null || question.getReply().isEmpty())) {
            question.setReply(reply);
            service.save(question);
        }
        return "redirect:/questions";
    }

    // Xóa câu hỏi
    @PostMapping("/questions/delete")
    public String deleteQuestion(@RequestParam("id") Long id) {
        service.deleteById(id);
        return "redirect:/questions";
    }
}
