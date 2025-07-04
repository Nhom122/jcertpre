package com.jcertpre.controller;

import com.jcertpre.model.Exam;
import com.jcertpre.model.ExamResult;
import com.jcertpre.services.ExamService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ExamController {

    @Autowired
    private ExamService examService;

    // L6: Hiển thị danh sách đề thi
    @GetMapping("/exams")
    public String listExams(Model model) {
        List<Exam> exams = examService.getAllExams();
        model.addAttribute("exams", exams);
        return "exam";
    }

    // L6: Hiển thị trang làm bài thi
    @GetMapping("/exams/{id}")
    public String takeExam(@PathVariable("id") Long examId, Model model) {
        Exam exam = examService.getExamById(examId);
        model.addAttribute("exam", exam);
        return "take_exam";
    }

    // L6: Nộp bài thi và chấm điểm
    @PostMapping("/exams/{id}/submit")
    public String submitExam(@PathVariable("id") Long examId,
                             @RequestParam List<String> answers,
                             HttpSession session,
                             Model model) {
        ExamResult result = examService.gradeExam(examId, answers);
        model.addAttribute("result", result);
        return "exam_result";
    }
}
