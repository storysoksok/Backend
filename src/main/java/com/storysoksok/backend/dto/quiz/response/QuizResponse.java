package com.storysoksok.backend.dto.quiz.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class QuizResponse {
    private List<UUID> quizId;
    private List<String> questionList;
    private List<List<String>> choiceList;
    private List<Integer> answerList;

    @Builder
    public QuizResponse(List<UUID> quizId, List<String> questionList, List<List<String>> choiceList, List<Integer> answerList) {
        this.quizId = quizId;
        this.questionList = questionList;
        this.choiceList = choiceList;
        this.answerList = answerList;
    }
}
