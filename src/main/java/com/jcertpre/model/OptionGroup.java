package com.jcertpre.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import java.util.List;

@Embeddable
public class OptionGroup {

    @ElementCollection
    @CollectionTable(name = "question_options")
    @Column(name = "option_text")
    private List<String> options;

    public OptionGroup() {}

    public OptionGroup(List<String> options) {
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}
