package com.storysoksok.backend.domain.postgre.quiz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.storysoksok.backend.domain.constants.Correct;
import com.storysoksok.backend.domain.postgre.BasePostgresEntity;
import com.storysoksok.backend.domain.postgre.member.Member;
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
 * 객관식 문항 하나단위로 저장되는 퀴즈
 */
public class Quiz extends BasePostgresEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID quizId;

    @Column(nullable = false)
    private String quizQuestions;  // 퀴즈 질문

    @Column(nullable = false)
    private Integer quizAnswer;  // 퀴즈 정답 (객관식)

    @Enumerated(EnumType.STRING)
    private Correct isCorrect;  // 정답 여부

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void updateIsCorrect(Correct correct) {
        this.isCorrect = correct;
    }
}
