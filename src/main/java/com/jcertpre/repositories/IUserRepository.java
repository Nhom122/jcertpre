package com.jcertpre.repositories;

import com.jcertpre.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(User.Role role);

    long countByRole(User.Role role);

    boolean existsByEmail(String email);
}