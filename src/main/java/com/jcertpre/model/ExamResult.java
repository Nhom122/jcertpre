package com.jcertpre.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class ExamResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;

    private double score;

    private LocalDate examDate;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int durationMinutes;

    @ElementCollection
    private List<String> submittedAnswers; // danh sách A/B/C/D học viên chọn

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    private boolean isRetake;

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public List<String> getSubmittedAnswers() { return submittedAnswers; }
    public void setSubmittedAnswers(List<String> submittedAnswers) { this.submittedAnswers = submittedAnswers; }

    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }

    public boolean isRetake() { return isRetake; }
    public void setRetake(boolean retake) { isRetake = retake; }
}