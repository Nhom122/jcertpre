package com.jcertpre.model;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "feedbacks")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User learner;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(columnDefinition = "TEXT")
    private String response;

    private LocalDateTime submittedAt = LocalDateTime.now();

    public enum Status {
        PENDING, IN_PROGRESS, RESOLVED
    }

    public Feedback() {}

    public Feedback(String response, Status status, String message, User learner) {
        this.response = response;
        this.status = status;
        this.message = message;
        this.learner = learner;
    }

    public Feedback(Long id, User learner, String message, Status status, String response) {
        this.id = id;
        this.learner = learner;
        this.message = message;
        this.status = status;
        this.response = response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getLearner() {
        return learner;
    }

    public void setLearner(User learner) {
        this.learner = learner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}