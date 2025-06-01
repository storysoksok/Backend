package com.storysoksok.backend.repository.quiz;

import com.storysoksok.backend.domain.postgre.quiz.QuizContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuizContentRepository extends JpaRepository<QuizContent, UUID> {
}
