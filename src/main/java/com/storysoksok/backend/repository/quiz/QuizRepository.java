package com.storysoksok.backend.repository.quiz;

import com.storysoksok.backend.domain.postgre.quiz.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuizRepository extends JpaRepository<Quiz, UUID> {
}
