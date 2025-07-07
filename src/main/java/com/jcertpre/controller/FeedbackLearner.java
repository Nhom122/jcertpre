package com.jcertpre.controller;

import com.jcertpre.model.Feedback;
import com.jcertpre.model.User;
import com.jcertpre.repositories.IFeedbackRepository;
import com.jcertpre.repositories.IUserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import static com.jcertpre.model.User.Role.ADVISOR;
import static com.jcertpre.model.User.Role.LEARNER;

@Controller
@RequestMapping("/feedback")
public class FeedbackLearner {

    @Autowired
    private IFeedbackRepository feedbackRepository;

    @Autowired
    private IUserRepository userRepository;

    // Gửi feedback
    @PostMapping("/submit")
    public String submitFeedback(@RequestParam("content") String content,
                                 HttpSession session,
                                 Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "login";
        }

        if (LEARNER != user.getRole()) {
            return "login";
        }

        User learner = userRepository.findById(user.getId()).orElse(null);

        if (learner == null) {
            model.addAttribute("error", "Không tìm thấy thông tin người dùng.");
            return "redirect:/learner/dashboard";
        }else if (learner.getRole() != LEARNER) {
            model.addAttribute("errorMessage", "Không tìm thấy học viên. Vui lòng kiểm tra lại mã số học viên hoặc đăng nhập lại.");
            return "redirect:/learner/dashboard";
        }

        Feedback feedback = new Feedback();
        feedback.setLearner(learner);
        feedback.setMessage(content);
        feedback.setStatus(Feedback.Status.PENDING);
        feedback.setSubmittedAt(java.time.LocalDateTime.now());

        feedbackRepository.save(feedback);

        model.addAttribute("success", "Đã gửi góp ý thành công!");
        return "redirect:/learner/dashboard";
    }
}
