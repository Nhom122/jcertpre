package com.jcertpre.services;

import com.jcertpre.model.Exam;
import com.jcertpre.model.ExamResult;
import com.jcertpre.repositories.IExamRepository;
import com.jcertpre.repositories.IExamResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExamService {

    @Autowired
    private IExamRepository examRepository;

    @Autowired
    private IExamResultRepository IExamResultRepository;

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

        int correctCount = 0;
        for (int i = 0; i < Math.min(correctAnswers.size(), answers.size()); i++) {
            if (correctAnswers.get(i).trim().equalsIgnoreCase(answers.get(i).trim())) {
                correctCount++;
            }
        }
        double score = ((double) correctCount / correctAnswers.size()) * 10.0;
        ExamResult result = new ExamResult();
        result.setStudentName("Anonymous"); // sau này bạn có thể set theo session
        result.setSubjectName(exam.getTitle()); // dùng title làm tên môn thi
        result.setScore(score);
        result.setExamDate(LocalDate.now());
        return IExamResultRepository.save(result);
    }

}