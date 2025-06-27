package com.example.demo.model;

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


    // ✅ GETTER và SETTER
    public Long getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getReason() {
        return reason;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
