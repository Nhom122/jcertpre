package com.jcertpre.controller;

import com.jcertpre.model.Exam;
import com.jcertpre.model.ExamResult;
import com.jcertpre.services.ExamResultService;
import com.jcertpre.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamResultService resultService;

    // Trang danh sách đề thi
    @GetMapping("/list")
    public String list(Model m) {
        m.addAttribute("exams", examService.getAllExams());
        return "exam_list";
    }

    // Trang tạo đề thi (form)
    @GetMapping("/create")
    public String createForm() {
        return "exam_create";
    }

    // Xử lý submit đề thi (POST)
    @PostMapping("/create")
    public String handleCreate(@RequestParam String title,
                               @RequestParam String description,
                               @RequestParam int durationMinutes,
                               @RequestParam String questionsRaw,
                               @RequestParam String answersRaw) {

        Exam exam = new Exam();
        exam.setTitle(title);
        exam.setDescription(description);
        exam.setDurationMinutes(durationMinutes);

        List<String> questions = Arrays.stream(questionsRaw.split("\\r?\\n"))
                .map(String::trim).filter(s -> !s.isEmpty())
                .toList();
        List<String> answers = Arrays.stream(answersRaw.split("\\r?\\n"))
                .map(String::trim).filter(s -> !s.isEmpty())
                .toList();

        if (questions.size() != answers.size()) {
            throw new IllegalArgumentException("Số câu hỏi và đáp án không khớp!");
        }

        exam.setQuestions(questions);
        exam.setCorrectAnswers(answers);
        examService.createExam(exam);

        return "redirect:/exam/list";
    }

    // Trang bắt đầu thi
    @GetMapping("/take/{id}")
    public String take(@PathVariable Long id, Model m) {
        Exam e = examService.getExamById(id);
        m.addAttribute("exam", e);
        return "exam_take";
    }

    // Submit bài thi
    @PostMapping("/submit")
    public String submit(@RequestParam Long examId,
                         @RequestParam("answers") List<String> answers,
                         @SessionAttribute("currentUser") String studentName, Model m) {
        Exam e = examService.getExamById(examId);
        ExamResult er = resultService.submitExam(e, studentName, answers);
        return "redirect:/exam/result/" + er.getId();
    }

    // Hiển thị kết quả
    @GetMapping("/result/{id}")
    public String result(@PathVariable Long id, Model m) {
        ExamResult er = resultService.findById(id);
        Exam exam = examService.getExamById(er.getExam().getId());
        m.addAttribute("exam", exam);
        m.addAttribute("examResult", er);
        return "exam_result";
    }

    // Xem lại bài thi
    @GetMapping("/review/{id}")
    public String review(@PathVariable Long id, Model m) {
        ExamResult er = resultService.findById(id);
        Exam exam = examService.getExamById(er.getExam().getId());
        m.addAttribute("exam", exam);
        m.addAttribute("examResult", er);
        return "exam_review";
    }

    // Thi lại (redirect trở lại trang thi)
    @GetMapping("/retry/{examId}")
    public String retry(@PathVariable Long examId) {
        return "redirect:/exam/take/" + examId;
    }
}
