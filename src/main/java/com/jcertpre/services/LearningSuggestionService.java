package com.jcertpre.services;

import com.jcertpre.model.ExamResult;
import com.jcertpre.repositories.IExamResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class LearningSuggestionService {

    @Autowired
    private IExamResultRepository examResultRepository;

    public Map<ExamResult, String> getSuggestions() {
        List<ExamResult> results = examResultRepository.findAll();
        Map<ExamResult, String> suggestionMap = new LinkedHashMap<>();

        for (ExamResult r : results) {
            double score = r.getScore();
            String suggest;

            if (score < 5) {
                suggest = "Ôn tập lại kiến thức cơ bản";
            } else if (score < 7) {
                suggest = "Nên học thêm bài tập nâng cao";
            } else if (score < 8.5) {
                suggest = "Có thể học phần nâng cao hoặc học nhóm";
            } else {
                suggest = "Đủ khả năng học tiếp môn kế tiếp";
            }

            suggestionMap.put(r, suggest);
        }

        return suggestionMap;
    }
}