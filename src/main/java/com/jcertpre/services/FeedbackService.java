package com.jcertpre.services;

import com.jcertpre.model.Feedback;
import com.jcertpre.model.User;
import com.jcertpre.repositories.IFeedbackRepository;
import com.jcertpre.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class FeedbackService {
    @Autowired
    private IFeedbackRepository feedbackRepository;

    @Autowired
    private IUserRepository userRepository;

    // Thêm phản ánh
    public Feedback submitFeedback(Long learnerId, String message) {
        try {
            User learner = userRepository.findById(learnerId)
                    .orElseThrow(() -> new RuntimeException("Học viên không tồn tại"));

            Feedback feedback = new Feedback();
            feedback.setLearner(learner);
            feedback.setMessage(message);
            feedback.setStatus(Feedback.Status.PENDING);
            feedback.setSubmittedAt(LocalDateTime.now());

            return feedbackRepository.save(feedback);
        } catch (Exception e) {
            throw new RuntimeException("Không thể gửi phản ánh: " + e.getMessage());
        }
    }

    // Lấy danh sách phản ánh chưa xử lý
    public List<Feedback> getAllPendingFeedback() {
        return feedbackRepository.findByStatus(Feedback.Status.PENDING);
    }

    // Giải quyết phản ánh (thêm ghi chú và đánh dấu đã xử lý)
    public Feedback respondToFeedback(Long feedbackId, String response) {
        try {
            Feedback feedback = feedbackRepository.findById(feedbackId)
                    .orElseThrow(() -> new RuntimeException("Phản ánh không tồn tại"));

            feedback.setResponse(response);
            feedback.setStatus(Feedback.Status.RESOLVED);
            return feedbackRepository.save(feedback);
        } catch (Exception e) {
            throw new RuntimeException("Không thể phản hồi phản ánh: " + e.getMessage());
        }
    }
}