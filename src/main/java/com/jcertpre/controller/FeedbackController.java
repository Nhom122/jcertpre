package com.jcertpre.controller;

import com.jcertpre.model.Feedback;
import com.jcertpre.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // Trang hiển thị danh sách phản hồi chưa xử lý
    @GetMapping("/pending")
    public String showPendingFeedbacks(Model model) {
        List<Feedback> feedbacks = feedbackService.getAllByStatus();
        model.addAttribute("feedbacks", feedbacks);
        return "Admin_phanhoihocvien";
    }

    // Trả về form trả lời phản hồi
    @GetMapping("/respond/{id}")
    public String showRespondForm(@PathVariable Long id, Model model) {
        Feedback feedback = feedbackService.getFeedbackById(id);
        model.addAttribute("feedback", feedback);
        return "Admin_traloiphanhoi"; // <- file HTML form trả lời
    }

    // Xử lý phản hồi sau khi admin gửi form
    @PostMapping("/respond/{id}")
    public String respondToFeedback(@PathVariable Long id,
                                    @RequestParam("response") String response) {
        feedbackService.respondToFeedback(id, response);
        return "redirect:/admin/feedback/pending";
    }
}
