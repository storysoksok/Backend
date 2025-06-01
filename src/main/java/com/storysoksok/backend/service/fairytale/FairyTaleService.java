package com.storysoksok.backend.service.fairytale;

import com.storysoksok.backend.domain.constants.FairyTaleCharacter;
import com.storysoksok.backend.domain.constants.FairyTaleLocation;
import com.storysoksok.backend.domain.constants.FairyTaleSubject;
import com.storysoksok.backend.domain.constants.SecondHalfRecommendStory;
import com.storysoksok.backend.domain.postgre.fairytale.FairyTale;
import com.storysoksok.backend.domain.postgre.fairytale.FairyTaleImage;
import com.storysoksok.backend.domain.postgre.fairytale.FairyTaleStory;
import com.storysoksok.backend.domain.postgre.member.Member;
import com.storysoksok.backend.domain.redis.MidPartFairyTale;
import com.storysoksok.backend.dto.fairytale.request.FairyTaleCreateRequest;
import com.storysoksok.backend.dto.fairytale.request.SecondHalfFairyTaleRequest;
import com.storysoksok.backend.dto.fairytale.response.FairyTaleImageResponse;
import com.storysoksok.backend.dto.fairytale.response.FairyTaleResponse;
import com.storysoksok.backend.dto.fairytale.response.FirstFairyTaleResponse;
import com.storysoksok.backend.dto.fairytale.response.SecondHalfFairyTaleResponse;
import com.storysoksok.backend.exception.CustomException;
import com.storysoksok.backend.exception.ErrorCode;
import com.storysoksok.backend.repository.fairytale.FairyTaleImageRepository;
import com.storysoksok.backend.repository.fairytale.FairyTaleRepository;
import com.storysoksok.backend.repository.fairytale.FairyTaleStoryRepository;
import com.storysoksok.backend.repository.redis.RedisFairyTaleRepository;
import com.storysoksok.backend.service.gpt.GptService;
import com.storysoksok.backend.util.prompt.Prompt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class FairyTaleService {
    private final RedisFairyTaleRepository redisFairyTaleRepository;
    private final FairyTaleRepository fairyTaleRepository;
    private final FairyTaleStoryRepository fairyTaleStoryRepository;
    private final FairyTaleImageRepository fairyTaleImageRepository;
    private final Prompt prompt;
    private final GptService gptService;
    private final static Integer FIRST_PAGE_NUM = 1;
    private final static Integer SECOND_HALF_PAGE_NUM = 5;
    @Value("${fairy-tale.id}")
    private UUID fairyTaleId;

    /**
     * 원하는 동화 중반까지 생성
     * 1. 작성된 프롬프트에 동화책 생성 요청
     * 2. 중반부 동화 내용 RedisHash에 임시 저장
     * 3. 동화내용을 프롬프팅해서 이미지 생성 (첫페이지 이미지만 생성)
     * 4. 이미지 RedisHash에 임시 저장 (List)
     * 5. response로 클라이언트에게 전송
     */
    public FirstFairyTaleResponse firstFairyTale(FairyTaleCreateRequest request, Member member) {
        Map<String, String> map = new HashMap<>();

        /**
         * 유효성 검증
         */
        // 빈 문자열 체크
        boolean hasOtherCharacter =  StringUtils.hasText(request.getOtherCharacter());
        boolean hasOtherLocation  =  StringUtils.hasText(request.getOtherLocation());
        boolean hasOtherSubject   =  StringUtils.hasText(request.getOtherSubject());

        map.put("FairyTaleCharacter",
                request.getFairyTaleCharacter() == FairyTaleCharacter.ETC && hasOtherCharacter
                        ? request.getOtherCharacter()
                        : request.getFairyTaleCharacter().getDescription());   // ← Enum → String

        map.put("FairyTaleLocation",
                request.getFairyTaleLocation() == FairyTaleLocation.ETC && hasOtherLocation
                        ? request.getOtherLocation()
                        : request.getFairyTaleLocation().getDescription());

        map.put("FairyTaleSubject",
                request.getFairyTaleSubject() == FairyTaleSubject.ETC && hasOtherSubject
                        ? request.getOtherSubject()
                        : request.getFairyTaleSubject().getDescription());

        log.debug("캐릭터 : {}", map.get("FairyTaleCharacter"));
        log.debug("장소   : {}", map.get("FairyTaleLocation"));
        log.debug("주제   : {}", map.get("FairyTaleSubject"));

        /* 프롬프팅 시작 및 동화 내용 반환 */
        String prompt = this.prompt.firstFairyTaleFormat(map);

        String firstStory = gptService.chatWithChatBot(prompt).orElseThrow(()
                -> {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        });

        /**
         * 중반부까지 이야기, 후반 추천부 나누기
         */
        String[] parts = firstStory.split("(?m)^### RECOMMEND ###$");
        String midPart  = parts[0].trim();        // 중반부
        String recPart  = parts.length > 1 ? parts[1].trim() : "";


        log.debug("중반까지의 전체 이야기 : {}", firstStory);


        Pattern lineSplitter = Pattern.compile("(?m)^\\d+\\s+");

        List<String> pageStoryList = lineSplitter.splitAsStream(midPart)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());

        List<String> recommendList = lineSplitter.splitAsStream(recPart)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());

        List<String> imageList = new ArrayList<>();

        /* 동화 중반부 까지 기준 이미지 생성 시도 */
        String pageStory = pageStoryList.get(0);
        log.debug("이야기 : {}", pageStory);

        log.debug("후반부 이야기 추천: {}", recommendList);

        String imgPrompt = this.prompt.fairyTaleImageFormat(pageStory, 1);

        String imageUrl = gptService.generatePicture(imgPrompt);
        imageList.add(imageUrl);

        log.debug("이미지 데이터 : {}", imageList);

        MidPartFairyTale midPartFairyTale = redisFairyTaleRepository.save(MidPartFairyTale.builder()
                .fairyTaleSubject(map.get("FairyTaleSubject"))
                .fairyTaleLocation(map.get("FairyTaleLocation"))
                .fairyTaleCharacter(map.get("FairyTaleCharacter"))
                .secondHalfRecommendStory(recommendList)
                .imgList(imageList)
                .pageStory(pageStoryList)
                .build());

        return FirstFairyTaleResponse.builder()
                .memberId(member.getMemberId())
                .imageUrl(imageUrl)
                .secondHalfRecommendStory(recommendList)
                .pageNumber(FIRST_PAGE_NUM)
                .memberName(member.getName())
                .midPartFairyTaleId(midPartFairyTale.getId())
                .midPartFairyTaleStory(pageStoryList)
                .build();
    }

    /**
     * 페이지별 이미지를 생성하는 로직
     */
    @Transactional
    public FairyTaleImageResponse createFairyTaleImage(Member member, UUID fairyTaleId, Integer pageNum) {

        if (pageNum > 8) {
            throw new CustomException(ErrorCode.PAGE_OUT_OF_RANGE);
        }
        /* 1) 중반부(REDIS)에 있는지 먼저 확인 */
        Optional<MidPartFairyTale> midOpt = redisFairyTaleRepository.findById(fairyTaleId);
        if (midOpt.isPresent()) {
            log.debug("중반부 동화책 감지");
            return createImageForMidPart(member, midOpt.get(), pageNum);
        }

        log.debug("후반부 동화책 감지");
        /* 2) 없으면 후반부(RDB) 조회 */
        FairyTale fairyTale = fairyTaleRepository.findById(fairyTaleId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FAIRY_TALE));

        return createImageForFinalPart(member, fairyTale, pageNum);
    }

    /**
     * 동화책 후반부에 대한 이미지를 담당하는 로직
     */
    private FairyTaleImageResponse createImageForFinalPart(Member member, FairyTale fairyTale, Integer pageNum) {
        List<FairyTaleStory> storyList = fairyTaleStoryRepository.findAllByFairyTaleOrderByPageNumAsc(fairyTale);

        List<String> storySoFar = storyList.stream()
                .limit(pageNum - 1)
                .map(FairyTaleStory::getContent)
                .toList();

        if (pageNum < 1 || pageNum > 8) {
            throw new CustomException(ErrorCode.PAGE_OUT_OF_RANGE);
        }

        String presentContent = storyList.get(pageNum - 1).getContent();

        log.debug("현재 생성할 페이지 이야기 : {}", presentContent);

        String prompt = this.prompt.fairyTaleImageFormat(storySoFar, pageNum, presentContent);

        String imageUrl = gptService.generatePicture(prompt);

        fairyTaleImageRepository.save(FairyTaleImage.builder()
                .pageNum(pageNum)
                .fairyTale(fairyTale)
                .imageUrl(imageUrl)
                .build());

        return buildResponse(member, fairyTale.getFairyTaleId(), imageUrl, pageNum);
    }

    /**
     * 동화책 중반부까지의 이미지 생성을 담당하는 로직
     */
    @Transactional
    public FairyTaleImageResponse createImageForMidPart(Member member, MidPartFairyTale midPartFairyTale, Integer pageNum) {
        List<String> fullStory = midPartFairyTale.getPageStory();

        List<String> storySoFar = fullStory.subList(0, pageNum - 1);
        log.debug("이전 페이지 내용들 : {}", storySoFar);


        String presentContent = fullStory.get(pageNum - 1);
        log.debug("현재 생성할 페이지 이야기 : {}", presentContent);

        String prompt = this.prompt.fairyTaleImageFormat(storySoFar, pageNum, presentContent);

        String url = gptService.generatePicture(prompt);
        midPartFairyTale.addImage(url);

        redisFairyTaleRepository.save(midPartFairyTale);

        return buildResponse(member, midPartFairyTale.getId(), url, pageNum);
    }

    /**
     * 동화책 후반부 이야기 생성
     */
    @Transactional
    public SecondHalfFairyTaleResponse secondHalfFairyTale(SecondHalfFairyTaleRequest request, Member member) {
        UUID midPartFairyTaleId = request.getMidPartFairyTaleId();
        MidPartFairyTale midPartFairyTale = redisFairyTaleRepository.findById(midPartFairyTaleId).orElseThrow(
                () -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_FAIRY_TALE);
                }
        );
        // 전체 추천 선택지 추출
        List<String> recommendStory = midPartFairyTale.getSecondHalfRecommendStory();

        String secondHalfRecommendStory = "";

        // 선택지별 후반부 동화내용 세팅
        if (request.getSecondHalfRecommendStory().equals(SecondHalfRecommendStory.FIRST_HALF_RECOMMEND_STORY)) {
            secondHalfRecommendStory = recommendStory.get(0);
        } else if (request.getSecondHalfRecommendStory().equals(SecondHalfRecommendStory.SECOND_HALF_RECOMMEND_STORY)) {
            secondHalfRecommendStory = recommendStory.get(1);
        } else if (request.getSecondHalfRecommendStory().equals(SecondHalfRecommendStory.ETC) &&
                !request.getOtherRecommendStory().isBlank()) {
            secondHalfRecommendStory = request.getOtherRecommendStory();
        } else if (request.getSecondHalfRecommendStory().equals(SecondHalfRecommendStory.THIRD_HALF_RECOMMEND_STORY)) {
            secondHalfRecommendStory = recommendStory.get(2);
        } else {
            throw new CustomException(ErrorCode.INVALID_RECOMMEND_TYPE);
        }

        // 중반부 이야기 추출
        List<String> midStoryList = midPartFairyTale.getPageStory();

        log.debug("후반부 이야기 : {}", secondHalfRecommendStory);

        String prompt = this.prompt.secondHalfFairyTaleStory(midStoryList, secondHalfRecommendStory);

        String secondStory = gptService.chatWithChatBot(prompt).orElseThrow(()
                -> {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        });

        /**
         * 중반부까지 이야기, 후반 추천부 나누기
         */
        String[] parts = secondStory.split("(?m)^### TITLE ###$");
        String secStory  = parts[0].trim();        // 중반부
        String title  = parts.length > 1 ? parts[1].trim() : "";


        log.debug("후반 이야기(결말포함) :{}", secondStory);
        Pattern lineSplitter = Pattern.compile("(?m)^\\d+\\s+");

        List<String> secondStoryList = lineSplitter.splitAsStream(secStory)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());

        log.debug("동화책 제목 : {}", title);

        FairyTale fairyTale = FairyTale.builder()
                .title(title)
                .member(member)
                .fairyTaleSubject(midPartFairyTale.getFairyTaleSubject())
                .fairyTaleCharacter(midPartFairyTale.getFairyTaleCharacter())
                .fairyTaleLocation(midPartFairyTale.getFairyTaleLocation())
                .build();

        fairyTaleRepository.save(fairyTale);

        /**
         * 동화 이야기 전체저장
         */
        List<String> allPages = Stream.concat(midStoryList.stream(), secondStoryList.stream())
                .toList();

        List<FairyTaleStory> stories =
                IntStream.range(0, allPages.size())
                        .mapToObj(i -> FairyTaleStory.builder()
                                .fairyTale(fairyTale)
                                .pageNum(i + 1)
                                .content(allPages.get(i))
                                .build())
                        .collect(Collectors.toList());

        fairyTaleStoryRepository.saveAll(stories);

        // 해당 페이지 이미지 생성
        String presentPageStory = secondStoryList.get(0);

        log.debug("디버깅용 이미지 요청 스토리 : {}", presentPageStory);

        String imagePrompt = this.prompt.fairyTaleImageFormat(midStoryList, SECOND_HALF_PAGE_NUM, presentPageStory);
        String imageUrl = gptService.generatePicture(imagePrompt);

        midPartFairyTale.addImage(imageUrl);
        // 1) 이미지 리스트 꺼내오기
        List<String> imgList = midPartFairyTale.getImgList();


        /**
         * 후반부 시작시 현재까지 이미지 RDB에 전체저장
         */
        midPartFairyTale.addImage(imageUrl);

        // 3) 0부터 4까지 (총 5번) 반복하면서 DB 저장용 객체 생성
        List<FairyTaleImage> fairyTaleImageList =
                IntStream.range(0, 5)
                        .mapToObj(i -> FairyTaleImage.builder()
                                .imageUrl(imgList.get(i))
                                .fairyTale(fairyTale)
                                .pageNum(i + 1)
                                .build())
                        .collect(Collectors.toList());

        fairyTaleImageRepository.saveAll(fairyTaleImageList);

        return SecondHalfFairyTaleResponse.builder()
                .secondHalfFairyTaleId(fairyTale.getFairyTaleId())
                .secondHalfFairyTaleStory(secondStoryList)
                .fairyTaleTitle(fairyTale.getTitle())
                .pageNumber(SECOND_HALF_PAGE_NUM)
                .memberName(member.getName())
                .memberId(member.getMemberId())
                .imageUrl(imageUrl)
                .build();
    }

    /**
     * 단순 매퍼 클래스
     */
    private FairyTaleImageResponse buildResponse(Member m, UUID id,
                                                 String url, int pageNum) {
        return FairyTaleImageResponse.builder()
                .midPartFairyTaleId(id)
                .imageUrl(url)
                .memberName(m.getName())
                .memberId(m.getMemberId())
                .pageNum(pageNum)
                .build();
    }

    /**
     * 테스팅용 동화책 생성(하는 척)
     */
    @Transactional
    public FairyTaleResponse getFairyTale(Integer pageNum) {
        if (pageNum == null) {
            throw new CustomException(ErrorCode.PAGE_OUT_OF_RANGE);
        }

        FairyTale fairyTale = fairyTaleRepository.findById(fairyTaleId).orElseThrow(
                () -> {
                    throw new CustomException(ErrorCode.NOT_FOUND_FAIRY_TALE);
                }
        );

        FairyTaleImage fairyTaleImage = fairyTaleImageRepository.findByFairyTaleAndPageNum(fairyTale, pageNum);
        FairyTaleStory fairyTaleStory = fairyTaleStoryRepository.findByFairyTaleAndPageNum(fairyTale, pageNum);

        try {
            Thread.sleep(4_000);  // 4초 (밀리초 단위)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return FairyTaleResponse.builder()
                .fairyTaleId(fairyTaleId)
                .content(fairyTaleStory.getContent())
                .pageNum(pageNum)
                .imageUrl(fairyTaleImage.getImageUrl())
                .title(fairyTale.getTitle())
                .build();
    }
}
