package com.jcertpre.model;


import jakarta.persistence.*;
import jakarta.persistence.Id;
import java.util.List;

@Entity
public class OptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @OneToMany(mappedBy = "optionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;

    public OptionGroup() {
    }

    public OptionGroup(Long id, List<Option> options, Exam exam, String title) {
        this.id = id;
        this.options = options;
        this.exam = exam;
        this.title = title;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}