package com.jcertpre.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "livestreams")
public class Livestream {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String meetingLink;

    private LocalDateTime scheduledAt;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private User instructor;

    public Livestream() {}

    public Livestream(String title, String description, String meetingLink, LocalDateTime scheduledAt, User instructor) {
        this.title = title;
        this.description = description;
        this.meetingLink = meetingLink;
        this.scheduledAt = scheduledAt;
        this.instructor = instructor;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getMeetingLink() { return meetingLink; }

    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }

    public LocalDateTime getScheduledAt() { return scheduledAt; }

    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }

    public User getInstructor() { return instructor; }

    public void setInstructor(User instructor) { this.instructor = instructor; }
}
