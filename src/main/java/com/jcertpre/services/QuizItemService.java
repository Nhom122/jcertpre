package com.jcertpre.services;

import com.jcertpre.model.QuizItem;
import com.jcertpre.repositories.IQuizItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizItemService {

    @Autowired
    private IQuizItemRepository quizItemRepo;

    public List<QuizItem> getAllQuizItems() {
        return quizItemRepo.findAll();
    }

    public QuizItem save(QuizItem quizItem) {
        return quizItemRepo.save(quizItem);
    }
}