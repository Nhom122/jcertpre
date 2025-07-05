package com.jcertpre.controller;

import com.jcertpre.model.Exam;
import com.jcertpre.model.QuizItem;
import com.jcertpre.model.ExamResult;
import com.jcertpre.services.ExamResultService;
import com.jcertpre.services.ExamService;
import com.jcertpre.services.QuizItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamResultService resultService;

    @Autowired
    private QuizItemService quizItemService;

    @GetMapping("/list")
    public String list(Model m) {
        m.addAttribute("exams", examService.getAllExams());
        return "exam_list";
    }

    @GetMapping("/create")
    public String createForm(Model m) {
        List<QuizItem> quizItems = quizItemService.getAllQuizItems();
        m.addAttribute("quizItems", quizItems);
        return "exam_create";
    }

    @PostMapping("/create")
    public String handleCreate(@RequestParam String title,
                               @RequestParam String description,
                               @RequestParam int durationMinutes,
                               @RequestParam(value = "quizItemIds", required = false) List<Long> quizItemIds) {

        if (quizItemIds == null || quizItemIds.isEmpty()) {
            throw new IllegalArgumentException("Phải chọn ít nhất 1 câu hỏi!");
        }

        List<QuizItem> selectedQuizItems = quizItemService.getAllQuizItems()
                .stream()
                .filter(q -> quizItemIds.contains(q.getId()))
                .toList();

        Exam exam = new Exam();
        exam.setTitle(title);
        exam.setDescription(description);
        exam.setDurationMinutes(durationMinutes);
        exam.setQuizItems(selectedQuizItems);

        examService.createExam(exam);

        return "redirect:/exam/list";
    }

    // ----- PHẦN SỬA -----

    // Hiển thị form sửa đề thi với dữ liệu hiện có
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model m) {
        Exam exam = examService.getExamById(id);
        List<QuizItem> quizItems = quizItemService.getAllQuizItems();

        m.addAttribute("exam", exam);
        m.addAttribute("quizItems", quizItems);
        return "exam_edit";
    }

    // Xử lý submit form sửa đề thi
    @PostMapping("/edit/{id}")
    public String handleEdit(@PathVariable Long id,
                             @RequestParam String title,
                             @RequestParam String description,
                             @RequestParam int durationMinutes,
                             @RequestParam(value = "quizItemIds", required = false) List<Long> quizItemIds) {

        if (quizItemIds == null || quizItemIds.isEmpty()) {
            throw new IllegalArgumentException("Phải chọn ít nhất 1 câu hỏi!");
        }

        Exam exam = examService.getExamById(id);
        exam.setTitle(title);
        exam.setDescription(description);
        exam.setDurationMinutes(durationMinutes);

        List<QuizItem> selectedQuizItems = quizItemService.getAllQuizItems()
                .stream()
                .filter(q -> quizItemIds.contains(q.getId()))
                .toList();

        exam.setQuizItems(selectedQuizItems);

        examService.createExam(exam); // dùng createExam vì save() trong service có thể update

        return "redirect:/exam/list";
    }

    // ----- PHẦN XÓA -----

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        examService.deleteExamById(id);
        return "redirect:/exam/list";
    }


    // Các method liên quan thi, submit, xem kết quả giữ nguyên

    @GetMapping("/take/{id}")
    public String take(@PathVariable Long id, Model m) {
        Exam e = examService.getExamById(id);
        m.addAttribute("exam", e);
        return "exam_take";
    }

    @PostMapping("/submit")
    public String submit(@RequestParam Long examId,
                         @RequestParam("answers") List<String> answers,
                         @SessionAttribute("currentUser") String studentName, Model m) {
        Exam e = examService.getExamById(examId);
        ExamResult er = resultService.submitExam(e, studentName, answers);
        return "redirect:/exam/result/" + er.getId();
    }

    @GetMapping("/result/{id}")
    public String result(@PathVariable Long id, Model m) {
        ExamResult er = resultService.findById(id);
        Exam exam = examService.getExamById(er.getExam().getId());
        m.addAttribute("exam", exam);
        m.addAttribute("examResult", er);
        return "exam_result";
    }

    @GetMapping("/review/{id}")
    public String review(@PathVariable Long id, Model m) {
        ExamResult er = resultService.findById(id);
        Exam exam = examService.getExamById(er.getExam().getId());
        m.addAttribute("exam", exam);
        m.addAttribute("examResult", er);
        return "exam_review";
    }

    @GetMapping("/retry/{examId}")
    public String retry(@PathVariable Long examId) {
        return "redirect:/exam/take/" + examId;
    }
}