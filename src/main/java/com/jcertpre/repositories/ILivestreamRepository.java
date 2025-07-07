package com.jcertpre.repositories;

import com.jcertpre.model.Livestream;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILivestreamRepository extends JpaRepository<Livestream, Long> {
    List<Livestream> findByInstructorId(Long instructorId);
}
