package com.jcertpre.services;

import com.jcertpre.model.Exam;
import com.jcertpre.repositories.IExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {
    @Autowired
    private IExamRepository examRepo;

    public Exam createExam(Exam exam) {
        return examRepo.save(exam);
    }

    public List<Exam> getAllExams() {
        return examRepo.findAll();
    }

    public Exam getExamById(Long id) {
        return examRepo.findById(id).orElseThrow(() -> new RuntimeException("Exam không tồn tại"));
    }

    public void deleteExamById(Long id) {
        examRepo.deleteById(id);
    }
}