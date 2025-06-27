package com.example.demo.service;

import com.example.demo.model.ExamResult;
import com.example.demo.repository.ExamResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamResultService {

    @Autowired
    private ExamResultRepository repo;

    public List<ExamResult> getAllResults() {
        return repo.findAll();
    }
}
