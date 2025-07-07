package com.jcertpre.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "StudentName")
    private String studentName;

    @Column(name = "Email")
    private String email;

    @Column(name = "Date")
    private LocalDate date;

    @Column(name = "TimeSlot")
    private String timeSlot;

    @Column(name = "Reason")
    private String reason;

    @Column(name = "Status")
    private String status = "PENDING"; // Mặc định là chờ duyệt

    @Column(name = "RejectionReason")
    private String rejectionReason;

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}