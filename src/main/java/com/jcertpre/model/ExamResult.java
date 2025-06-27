package com.jcertpre.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;
    private String subjectName;
    private Double score;
    private LocalDate examDate;

    // GETTER
    public Long getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public Double getScore() {
        return score;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    // SETTER
    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

}