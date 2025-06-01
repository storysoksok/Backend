package com.storysoksok.backend.dto.quiz.response;

import com.storysoksok.backend.domain.constants.Correct;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class QuizSolveResponse {
    private UUID memberId;
    private UUID quizId;
    private Correct isCorrect;
    private Integer userAnswer;
    private Integer quizAnswer;

    @Builder
    public QuizSolveResponse(UUID memberId, UUID quizId, Correct isCorrect, Integer userAnswer, Integer quizAnswer) {
        this.memberId = memberId;
        this.quizId = quizId;
        this.isCorrect = isCorrect;
        this.userAnswer = userAnswer;
        this.quizAnswer = quizAnswer;
    }
}
