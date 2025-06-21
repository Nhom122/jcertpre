package com.jcertpre.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "study_plans")
public class StudyPlan {
    public enum CertificateType {
        JLPT, NAT_TEST
    }

    public enum Level {
        N5, N4, N3, N2, N1
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User learner;

    @ManyToOne
    private User advisor;

    @Enumerated(EnumType.STRING)
    private CertificateType certificateType;

    @Enumerated(EnumType.STRING)
    private Level level;

    private Integer targetScore;

    private LocalDate deadline;

    public StudyPlan() {}

    public StudyPlan(User learner, User advisor, CertificateType certificateType, Level level, Integer targetScore, LocalDate deadline) {
        this.learner = learner;
        this.advisor = advisor;
        this.certificateType = certificateType;
        this.level = level;
        this.targetScore = targetScore;
        this.deadline = deadline;
    }

    public StudyPlan(Long id, User learner, User advisor, CertificateType certificateType, Level level, Integer targetScore, LocalDate deadline) {
        this.id = id;
        this.learner = learner;
        this.advisor = advisor;
        this.certificateType = certificateType;
        this.level = level;
        this.targetScore = targetScore;
        this.deadline = deadline;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getLearner() { return learner; }
    public void setLearner(User learner) { this.learner = learner; }

    public User getAdvisor() { return advisor; }
    public void setAdvisor(User advisor) { this.advisor = advisor; }

    public CertificateType getCertificateType() { return certificateType; }
    public void setCertificateType(CertificateType certificateType) { this.certificateType = certificateType; }

    public Level getLevel() { return level; }
    public void setLevel(Level level) { this.level = level; }

    public Integer getTargetScore() { return targetScore; }
    public void setTargetScore(Integer targetScore) { this.targetScore = targetScore; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
}