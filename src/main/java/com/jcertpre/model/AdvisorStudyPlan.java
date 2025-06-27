package com.jcertpre.model;

import jakarta.persistence.*;
@Entity
public class AdvisorStudyPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public User getAdvisor() {
        return advisor;
    }

    public void setAdvisor(User advisor) {
        this.advisor = advisor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getProgressNote() {
        return progressNote;
    }

    public void setProgressNote(String progressNote) {
        this.progressNote = progressNote;
    }

    public User getLearner() {
        return learner;
    }

    public void setLearner(User learner) {
        this.learner = learner;
    }

    private String goal;             // ví dụ: "Đạt N3 trong 2 tháng"
    private String strategy;         // nội dung chi tiết
    private String progressNote;     // nhận xét tiến độ

    @ManyToOne
    @JoinColumn(name = "learner_id")
    private User learner;

    @ManyToOne
    @JoinColumn(name = "advisor_id")
    private User advisor;

    // Constructors
    public AdvisorStudyPlan() {}

    public AdvisorStudyPlan(String goal, String strategy, String progressNote, User learner, User advisor) {
        this.goal = goal;
        this.strategy = strategy;
        this.progressNote = progressNote;
        this.learner = learner;
        this.advisor = advisor;
    }

// Getters và Setters
// (Bạn có thể dùng Lombok nếu cài sẵn)

}
