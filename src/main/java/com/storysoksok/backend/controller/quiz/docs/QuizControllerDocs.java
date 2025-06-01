package com.storysoksok.backend.controller.quiz.docs;

import com.storysoksok.backend.dto.oauth.request.CustomOAuth2User;
import com.storysoksok.backend.dto.quiz.request.QuizSolveRequest;
import com.storysoksok.backend.dto.quiz.response.QuizResponse;
import com.storysoksok.backend.dto.quiz.response.QuizSolveResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface QuizControllerDocs {

    @Operation(
            summary = "퀴즈 생성",
            description = """
                    
                    이 API는 인증이 필요합니다.

                    ### 요청 파라미터
                    - **fairy-tale-id** (String): 동화책ID [필수]
                    
                    ### 반환값
                    - **quizId** (List<String>): 저장된 퀴즈ID
                    - **questionList** (List<String>): 질문 리스트
                    - **choiceList** (List<List<String>>): 객관식 선택지 리스트
                    - **answerList** (List<Integer>): 각 퀴즈에 대한 정답 리스트
                    
                    ### 유의사항
                    - 동화책의 Id를 받아와 적절한 퀴즈를 반환합니다.
                    - 각 문제 순서대로 문제의 주제가 정해져있습니다.
                        1. 이해력: 이야기의 전개나 인과관계를 파악했는지 평가  
                        2. 기억력: 등장인물·장소·사건 순서 등 중요한 정보 기억 여부 평가  
                        3. 주의 집중력: 이야기의 중간·마지막 정보를 기반으로 집중력 평가  
                        4. 언어 추론 능력: 문맥 속 표현의 의미를 추론하는 능력 평가
                    - 추후 퀴즈 Id를 받아 퀴즈의 정답을 푸는 API와 연결됩니다.
 
                    """
    )
    ResponseEntity<QuizResponse> createQuiz(CustomOAuth2User customOAuth2User, UUID id);

    @Operation(
            summary = "퀴즈 정답 제출",
            description = """
            이 API는 **Access-Token 인증**이 필요합니다.

            ### 요청
            | 구분 | 이름 | 위치 | 타입 | 제약 | 설명 |
            |------|------|------|------|------|------|
            | 1 | **quiz-id** | Path | `UUID` | ✔ | 풀 퀴즈의 PK |
            | 2 | **userAnswer** | Body | `int` | 1~4 | 사용자가 고른 번호 |

            ### 응답 (`200 OK`)
            | 필드 | 타입 | 설명 |
            |------|------|------|
            | memberId   | `UUID`        | 답안을 제출한 회원 ID |
            | quizId     | `UUID`        | 푼 퀴즈 ID |
            | isCorrect  | `Correct`     | `CORRECT` / `UN_CORRECT` |
            | userAnswer | `Integer`     | 사용자가 제출한 번호 |
            | quizAnswer | `Integer`     | 실제 정답 번호 |

            ### 비고
            * 이미 채점된 퀴즈를 다시 제출하면 결과가 **마지막 제출**로 덮어써집니다.
            * 유효하지 않은 `quiz-id` 이거나 1-4 외 번호를 보내면 4xx 오류가 반환됩니다.
            """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채점 성공",
                    content = @Content(schema = @Schema(implementation = QuizSolveResponse.class))),
    })
    ResponseEntity<QuizSolveResponse> solveQuiz(
            @Parameter(hidden = true)
            CustomOAuth2User customOAuth2User, UUID id,
            @Valid
            QuizSolveRequest request
    );
}
