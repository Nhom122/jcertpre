package com.jcertpre.model;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {
    public enum CourseType {
        VIDEO, LIVESTREAM, OFFLINE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private CourseType courseType;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @Column(name = "approved")
    private Boolean approved = false;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    public Course() {}

    public Course(String title, String description, CourseType courseType, User instructor) {
        this.title = title;
        this.description = description;
        this.courseType = courseType;
        this.instructor = instructor;
    }

    public Course(Long id, String title, String description, CourseType courseType, User instructor) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.courseType = courseType;
        this.instructor = instructor;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public CourseType getCourseType() { return courseType; }
    public void setCourseType(CourseType courseType) { this.courseType = courseType; }

    public User getInstructor() { return instructor; }
    public void setInstructor(User instructor) { this.instructor = instructor; }

    public Boolean getApproved() { return approved; }
    public void setApproved(Boolean approved) { this.approved = approved; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}