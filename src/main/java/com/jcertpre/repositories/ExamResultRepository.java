package com.jcertpre.repositories;

import com.jcertpre.model.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
    // có thể thêm các truy vấn tùy chỉnh nếu cần
}
