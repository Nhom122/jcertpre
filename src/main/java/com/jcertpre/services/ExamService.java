package com.jcertpre.services;

import com.jcertpre.model.Exam;
import com.jcertpre.model.ExamResult;
import com.jcertpre.repositories.IExamRepository;
import com.jcertpre.repositories.ExamResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {

    @Autowired
    private IExamRepository examRepository;

    @Autowired
    private ExamResultRepository examResultRepository;

    // L6: Lấy tất cả đề thi từ DB
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    // L6: Lấy đề thi theo ID
    public Exam getExamById(Long id) {
        return examRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy đề thi" + id));
    }

    // L6: Chấm điểm bài thi
    public ExamResult gradeExam(Long examId, List<String> answers) {
        Exam exam = getExamById(examId);
        List<String> correctAnswers = exam.getCorrectAnswers();
        int score = 0;

        for (int i = 0; i < correctAnswers.size() && i < answers.size(); i++) {
            if (correctAnswers.get(i).trim().equalsIgnoreCase(answers.get(i).trim())) {
                score++;
            }
        }

        ExamResult result = new ExamResult(exam, score, correctAnswers.size(), "Anonymous");
        return examResultRepository.save(result);
    }
}

