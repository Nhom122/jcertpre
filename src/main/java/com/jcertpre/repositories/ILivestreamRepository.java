package com.jcertpre.repositories;

import com.jcertpre.model.Livestream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILivestreamRepository extends JpaRepository<Livestream, Long> {
    List<Livestream> findByInstructorId(Long instructorId);
}
