package com.storysoksok.backend.service.quiz;

import com.storysoksok.backend.domain.constants.Correct;
import com.storysoksok.backend.domain.postgre.fairytale.FairyTale;
import com.storysoksok.backend.domain.postgre.fairytale.FairyTaleStory;
import com.storysoksok.backend.domain.postgre.member.Member;
import com.storysoksok.backend.domain.postgre.quiz.Quiz;
import com.storysoksok.backend.domain.postgre.quiz.QuizContent;
import com.storysoksok.backend.dto.quiz.request.QuizSolveRequest;
import com.storysoksok.backend.dto.quiz.response.QuizResponse;
import com.storysoksok.backend.dto.quiz.response.QuizSolveResponse;
import com.storysoksok.backend.exception.CustomException;
import com.storysoksok.backend.exception.ErrorCode;
import com.storysoksok.backend.repository.fairytale.FairyTaleRepository;
import com.storysoksok.backend.repository.fairytale.FairyTaleStoryRepository;
import com.storysoksok.backend.repository.quiz.QuizContentRepository;
import com.storysoksok.backend.repository.quiz.QuizRepository;
import com.storysoksok.backend.service.gpt.GptService;
import com.storysoksok.backend.util.prompt.Prompt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
@Slf4j
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizContentRepository quizContentRepository;
    private final FairyTaleRepository fairyTaleRepository;
    private final FairyTaleStoryRepository fairyTaleStoryRepository;
    private final Prompt prompt;
    private final GptService gptService;


    /**
     * 퀴즈 생성 및 저장을 수행하는 API
     */
// Service 메서드 ----------------------------------------------------------
    @Transactional
    public QuizResponse createQuiz(Member member, UUID fairyTaleId) {

        FairyTale fairyTale = fairyTaleRepository.findById(fairyTaleId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FAIRY_TALE));

        List<String> storyList = fairyTaleStoryRepository
                .findAllByFairyTaleOrderByPageNumAsc(fairyTale).stream()
                .map(FairyTaleStory::getContent)
                .toList();

        String quizPrompt = prompt.createQuiz(storyList);

        String raw = gptService.chatWithChatBot(quizPrompt)
                .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));

        /* ------------------ 파싱 ------------------ */

        // 1) ANSWER 구분자로 앞·뒤 잘라내기
        String[] parts = raw.split("(?m)^### ANSWER ###\\s*$");
        if (parts.length != 2)
            throw new CustomException(ErrorCode.INVALID_QUIZ_FORMAT);

        String qaBlock = parts[0].trim();  // 문제+선택지 구간
        String answerBlock = parts[1].trim();  // 정답 구간

        List<String> questionList = new ArrayList<>(4);
        List<List<String>> choiceList = new ArrayList<>(4);

        // 2) 문제+선택지 라인 단위 스캔
        Pattern questionLine = Pattern.compile("^[1-4]\\s+.+\\?$");
        Pattern choiceLine = Pattern.compile("^1\\s+.+?2\\s+.+?3\\s+.+?4\\s+.+");

        Pattern optionSplit = Pattern.compile(
                "(?<=1\\s)(.+?)(?=\\s2\\s)|" +
                        "(?<=2\\s)(.+?)(?=\\s3\\s)|" +
                        "(?<=3\\s)(.+?)(?=\\s4\\s)|" +
                        "(?<=4\\s)(.+)$"
        );

        for (String line : qaBlock.split("\\R")) {   // \\R = 모든 개행
            line = line.trim();
            if (line.isEmpty()) continue;

            if (questionLine.matcher(line).matches()) {
                // "1 " 같은 번호 제거 후 저장
                questionList.add(line.replaceFirst("^[1-4]\\s+", ""));
            } else if (choiceLine.matcher(line).matches()) {
                // 1~4 선택지 추출
                List<String> opts = new ArrayList<>(4);
                Matcher m = optionSplit.matcher(line);
                while (m.find()) {
                    for (int i = 1; i <= 4; i++) {
                        String g = m.group(i);
                        if (g != null) {
                            opts.add(g.trim());
                            break;
                        }
                    }
                }
                if (opts.size() != 4)
                    throw new CustomException(ErrorCode.INVALID_QUIZ_FORMAT);
                choiceList.add(opts);
            }
        }

        if (questionList.size() != 4 || choiceList.size() != 4)
            throw new CustomException(ErrorCode.INVALID_QUIZ_FORMAT);

        // 3) 정답 파싱
        List<Integer> answerList = answerBlock.lines()
                .map(String::trim)
                .filter(s -> s.matches("[1-4]"))
                .map(Integer::parseInt)
                .toList();

        if (answerList.size() != 4)
            throw new CustomException(ErrorCode.INVALID_QUIZ_FORMAT);

        QuizResponse quizResponse = QuizResponse.builder()
                .choiceList(choiceList)
                .questionList(questionList)
                .answerList(answerList)
                .build();

        // 퀴즈 문제, 선택지, 정답 세팅
        // 4-1) Quiz 엔티티 4개 생성
        List<Quiz> quizList = IntStream.range(0, 4)
                .mapToObj(i -> Quiz.builder()
                        .quizQuestions(quizResponse.getQuestionList().get(i))// 문제 정보
                        .quizAnswer(quizResponse.getAnswerList().get(i))   // 정답 번호
                        .isCorrect(Correct.UNKNOWN) // 현재 미채점
                        .member(member)
                        .build()
                )
                .collect(Collectors.toList());

        quizRepository.saveAll(quizList);

        // 4-2) 선택지(QuizContent) 16개 생성
        List<QuizContent> contentList = new ArrayList<>(16);

        for (int i = 0; i < 4; i++) {
            Quiz quiz = quizList.get(i);
            List<String> opts = quizResponse.getChoiceList().get(i);   // 길이 4 보장
            for (String opt : opts) {
                contentList.add(QuizContent.builder()
                        .content(opt)
                        .quiz(quiz)
                        .build());
            }
        }

        quizContentRepository.saveAll(contentList);

        // DTO 재구성
        return QuizResponse.builder()
                .quizId(quizList.stream()
                        .map(Quiz::getQuizId)
                        .toList())
                .questionList(quizResponse.getQuestionList())   // 원래 순서 유지
                .choiceList(quizResponse.getChoiceList())
                .answerList(quizResponse.getAnswerList())
                .build();
    }

    @Transactional
    public QuizSolveResponse solveQuiz(Member member, UUID id, QuizSolveRequest request) {
        // 정답인지 아닌지 확인
        Quiz quiz = quizRepository.findById(id).orElseThrow(
                () -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_QUIZ);
                }
        );

        Integer quizAnswer = quiz.getQuizAnswer();
        // 정답시
        if (request.getUserAnswer().equals(quizAnswer)) {
            quiz.updateIsCorrect(Correct.CORRECT);
            return QuizSolveResponse.builder()
                    .memberId(member.getMemberId())
                    .userAnswer(request.getUserAnswer())
                    .quizAnswer(quizAnswer)
                    .isCorrect(quiz.getIsCorrect())
                    .quizId(quiz.getQuizId())
                    .build();

        } else { // 오답시
            quiz.updateIsCorrect(Correct.UN_CORRECT);
            return QuizSolveResponse.builder()
                    .memberId(member.getMemberId())
                    .userAnswer(request.getUserAnswer())
                    .quizAnswer(quizAnswer)
                    .isCorrect(quiz.getIsCorrect())
                    .quizId(quiz.getQuizId())
                    .build();
        }
    }
}
