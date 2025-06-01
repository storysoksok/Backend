package com.storysoksok.backend.domain.postgre.quiz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.storysoksok.backend.domain.postgre.BasePostgresEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
/**
 * 퀴즈의 객관식 선택지 여부
 */
public class QuizContent extends BasePostgresEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID quizContent;

    @Column(nullable = false)
    private String content;  // 퀴즈 질문 (1. ... 2. ...)

    @ManyToOne(fetch = FetchType.LAZY)
    private Quiz quiz;
}
