package com.jcertpre.services;

import com.jcertpre.model.Exam;
import com.jcertpre.model.ExamResult;
import com.jcertpre.repositories.IExamResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamResultService {
    @Autowired
    private IExamResultRepository resultRepo;

    public ExamResult submitExam(Exam exam, String studentName, List<String> answers) {
        LocalDateTime start = LocalDateTime.now();
        double score = 0;
        var quizItems = exam.getQuizItems();
        int n = quizItems.size();

        for (int i = 0; i < n; i++) {
            String correct = quizItems.get(i).getCorrectAnswer();
            String submitted = i < answers.size() ? answers.get(i) : null;

            if (submitted != null &&
                    correct != null &&
                    correct.trim().equalsIgnoreCase(submitted.trim())) {
                score++;
            }
        }

        double finalScore = (score / n) * 10.0;

        ExamResult er = new ExamResult();
        er.setExam(exam);
        er.setStudentName(studentName);
        er.setSubmittedAnswers(answers);
        er.setScore(finalScore);
        er.setStartTime(start);
        er.setEndTime(LocalDateTime.now());

        return resultRepo.save(er);
    }


    public List<ExamResult> findByStudent(String studentName) {
        return resultRepo.findByStudentName(studentName);
    }

    public ExamResult findById(Long id) {
        return resultRepo.findById(id).orElse(null);
    }
}