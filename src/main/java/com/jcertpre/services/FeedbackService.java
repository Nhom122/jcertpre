package com.jcertpre.services;

import com.jcertpre.model.Feedback;
import com.jcertpre.model.Feedback.Status;
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

    // Gửi phản ánh mới từ học viên
    public Feedback submitFeedback(Long learnerId, String message) {
        try {
            User learner = userRepository.findById(learnerId)
                    .orElseThrow(() -> new RuntimeException("Học viên không tồn tại"));

            Feedback feedback = new Feedback();
            feedback.setLearner(learner);
            feedback.setMessage(message);
            feedback.setStatus(Status.PENDING);
            feedback.setSubmittedAt(LocalDateTime.now());

            return feedbackRepository.save(feedback);
        } catch (Exception e) {
            throw new RuntimeException("Không thể gửi phản ánh: " + e.getMessage());
        }
    }

    // Lấy danh sách phản hồi đang chờ xử lý (PENDING)
    public List<Feedback> getAllPendingFeedback() {
        return feedbackRepository.findByStatus(Status.PENDING);
    }

    // Lấy phản hồi theo ID để trả lời
    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phản hồi với ID: " + id));
    }

    // Trả lời phản hồi và đánh dấu đã xử lý
    public Feedback respondToFeedback(Long feedbackId, String response) {
        try {
            Feedback feedback = feedbackRepository.findById(feedbackId)
                    .orElseThrow(() -> new RuntimeException("Phản hồi không tồn tại"));

            feedback.setResponse(response);
            feedback.setStatus(Status.RESOLVED);
            return feedbackRepository.save(feedback);
        } catch (Exception e) {
            throw new RuntimeException("Không thể phản hồi phản ánh: " + e.getMessage());
        }
    }

    // (Tùy chọn) Trả về tất cả phản hồi của học viên theo ID
    public List<Feedback> getFeedbacksByLearnerId(Long learnerId) {
        return feedbackRepository.findByLearner_Id(learnerId);
    }

    // Trong FeedbackService.java
    public long countPendingFeedbacks() {
        return feedbackRepository.countByStatus(Status.PENDING);
    }

    public List<Feedback> getAllByStatus() {
        return feedbackRepository.findByStatusOrderBySubmittedAtDesc(Feedback.Status.PENDING);
    }

}
