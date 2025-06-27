package com.jcertpre.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ElementCollection
    @CollectionTable(name = "exam_questions", joinColumns = @JoinColumn(name = "exam_id"))
    @Column(name = "question")
    private List<String> questions;

    @ElementCollection
    @CollectionTable(name = "exam_correct_answers", joinColumns = @JoinColumn(name = "exam_id"))
    @Column(name = "correct_answer")
    private List<String> correctAnswers;

    // Constructors
    public Exam() {
    }

    public Exam(String title, List<String> questions, List<String> correctAnswers) {
        this.title = title;
        this.questions = questions;
        this.correctAnswers = correctAnswers;
    }

// Getters & Setters

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

}
