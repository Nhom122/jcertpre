package com.jcertpre.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity

public class ConsultationSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;

    public ConsultationSchedule(String status, User learner, User advisor, String topic, LocalDateTime endTime, LocalDateTime startTime) {
        this.status = status;
        this.learner = learner;
        this.advisor = advisor;
        this.topic = topic;
        this.endTime = endTime;
        this.startTime = startTime;
    }

    public ConsultationSchedule(Long id, LocalDateTime startTime, LocalDateTime endTime, String topic, String status, User learner, User advisor) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.topic = topic;
        this.status = status;
        this.learner = learner;
        this.advisor = advisor;
    }

    private LocalDateTime endTime;
    private String topic;

    @ManyToOne
    @JoinColumn(name = "advisor_id")
    private User advisor;

    @ManyToOne
    @JoinColumn(name = "learner_id")
    private User learner;

    private String status; // PENDING, APPROVED, COMPLETED

    public ConsultationSchedule() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public User getAdvisor() {
        return advisor;
    }

    public void setAdvisor(User advisor) {
        this.advisor = advisor;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
