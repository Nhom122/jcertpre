package com.jcertpre.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class QuizItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text; // Nội dung câu hỏi

    // Các lựa chọn trả lời
    @ElementCollection
    private List<String> options;

    // Đáp án đúng, ví dụ "A", "B", "C", hoặc "D"
    private String correctAnswer;

    // Getter - Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    
    public String getCorrectOp(String correctAnswer) { 
        return switch (correctAnswer){
            case "A" -> this.options.get(0);
            case "B" -> this.options.get(1);
            case "C" -> this.options.get(2);
            case "D" -> this.options.get(3);
            default -> throw new IllegalStateException("Unexpected value: " + correctAnswer);
        };
    }
}