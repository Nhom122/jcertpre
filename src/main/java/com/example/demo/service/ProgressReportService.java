package com.example.demo.service;

import com.example.demo.model.ExamResult;
import com.example.demo.repository.ExamResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgressReportService {

    @Autowired
    private ExamResultRepository examResultRepository;

    public ReportDTO getProgressReport() {
        List<ExamResult> results = examResultRepository.findAll();

        int totalSubjects = results.size();
        double totalScore = results.stream().mapToDouble(ExamResult::getScore).sum();
        double avgScore = totalSubjects > 0 ? totalScore / totalSubjects : 0;

        String rating;
        if (avgScore >= 8) {
            rating = "Giỏi";
        } else if (avgScore >= 6.5) {
            rating = "Khá";
        } else if (avgScore >= 5) {
            rating = "Trung bình";
        } else {
            rating = "Yếu";
        }

        return new ReportDTO(totalSubjects, avgScore, rating);
    }

    // DTO chứa dữ liệu trả ra
    public static class ReportDTO {
        public int totalSubjects;
        public double averageScore;
        public String rating;

        public ReportDTO(int totalSubjects, double averageScore, String rating) {
            this.totalSubjects = totalSubjects;
            this.averageScore = averageScore;
            this.rating = rating;
        }

        public int getTotalSubjects() {
            return totalSubjects;
        }

        public double getAverageScore() {
            return averageScore;
        }

        public String getRating() {
            return rating;
        }
    }
}
