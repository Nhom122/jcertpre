package com.jcertpre.model;

import jakarta.persistence.*;

@Entity
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private boolean correct;

    @ManyToOne
    @JoinColumn(name = "option_group_id")
    private OptionGroup optionGroup;

    public Option() {
    }

    public Option(Long id, OptionGroup optionGroup, boolean correct, String text) {
        this.id = id;
        this.optionGroup = optionGroup;
        this.correct = correct;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OptionGroup getOptionGroup() {
        return optionGroup;
    }

    public void setOptionGroup(OptionGroup optionGroup) {
        this.optionGroup = optionGroup;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}