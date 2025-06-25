package com.jcertpre.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity

public class ConsultationSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;

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

// Getters và Setters

}
