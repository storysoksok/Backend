package com.storysoksok.backend.dto.quiz.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuizSolveRequest {
    @NotNull(message = "정답 입력은 필수입니다.")
    @Min(value = 1, message = "정답은 최소 1부터 4번까지 입니다.")
    @Max(value = 4, message = "정답은 최소 1부터 4번까지 입니다.")
    private Integer userAnswer;
}
