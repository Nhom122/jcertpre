package com.jcertpre.services;

import com.jcertpre.model.ExamResult;
import com.jcertpre.repositories.ExamResultRepository;
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
