package com.jcertpre.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User learner;

    @ManyToOne
    private Course course;

    private Double progress;

    private LocalDateTime enrolledAt;

    public Enrollment() {}

    public Enrollment(User learner, Course course, Double progress, LocalDateTime enrolledAt) {
        this.learner = learner;
        this.course = course;
        this.progress = progress;
        this.enrolledAt = enrolledAt;
    }

    public Enrollment(Long id, User learner, Course course, Double progress, LocalDateTime enrolledAt) {
        this.id = id;
        this.learner = learner;
        this.course = course;
        this.progress = progress;
        this.enrolledAt = enrolledAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getLearner() { return learner; }
    public void setLearner(User learner) { this.learner = learner; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public Double getProgress() { return progress; }
    public void setProgress(Double progress) { this.progress = progress; }

    public LocalDateTime getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(LocalDateTime enrolledAt) { this.enrolledAt = enrolledAt; }
}