package com.jcertpre.services;

import com.jcertpre.model.Question;
import com.jcertpre.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository repo;

    public void save(Question question) {
        repo.save(question);
    }

    public List<Question> getAll() {
        return repo.findAll();
    }

    public Question getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
