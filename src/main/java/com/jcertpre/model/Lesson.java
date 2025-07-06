package com.jcertpre.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String videoUrl; // Link video bài giảng
    private String slideUrl; // Link slide nếu có

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Lesson() {}

    public Lesson(String title, String videoUrl, String slideUrl, Course course) {
        this.title = title;
        this.videoUrl = videoUrl;
        this.slideUrl = slideUrl;
        this.course = course;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getVideoUrl() { return videoUrl; }

    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getSlideUrl() { return slideUrl; }

    public void setSlideUrl(String slideUrl) { this.slideUrl = slideUrl; }

    public Course getCourse() { return course; }

    public void setCourse(Course course) { this.course = course; }

    public void setAttachmentPath(String string) {
    }
}