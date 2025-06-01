package com.storysoksok.backend.controller.quiz.controller;

import com.storysoksok.backend.controller.quiz.docs.QuizControllerDocs;
import com.storysoksok.backend.domain.postgre.member.Member;
import com.storysoksok.backend.dto.oauth.request.CustomOAuth2User;
import com.storysoksok.backend.dto.quiz.request.QuizSolveRequest;
import com.storysoksok.backend.dto.quiz.response.QuizResponse;
import com.storysoksok.backend.dto.quiz.response.QuizSolveResponse;
import com.storysoksok.backend.service.quiz.QuizService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(
        name = "퀴즈 관련 API",
        description = "퀴즈 관련 API 제공"
)
public class QuizController implements QuizControllerDocs {
    private final QuizService quizService;

    @Override
    @PostMapping("/quiz/{fairy-tale-id}")
    public ResponseEntity<QuizResponse> createQuiz(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                   @PathVariable(value = "fairy-tale-id") UUID id) {

        Member member = customOAuth2User.getMember();
        return ResponseEntity.ok(quizService.createQuiz(member, id));
    }

    @Override
    @PatchMapping("/quiz/{quiz-id}")
    public ResponseEntity<QuizSolveResponse> solveQuiz(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                       @PathVariable(value = "quiz-id") UUID id,
                                                       @RequestBody @Valid QuizSolveRequest request) {

        Member member = customOAuth2User.getMember();
        return ResponseEntity.ok(quizService.solveQuiz(member, id, request));
    }
}
