package com.storysoksok.backend.service.fairytale;

import com.storysoksok.backend.domain.constants.FairyTaleCharacter;
import com.storysoksok.backend.domain.constants.FairyTaleLocation;
import com.storysoksok.backend.domain.constants.FairyTaleSubject;
import com.storysoksok.backend.domain.postgre.member.Member;
import com.storysoksok.backend.domain.redis.FirstFairyTale;
import com.storysoksok.backend.dto.fairytale.request.FairyTaleCreateRequest;
import com.storysoksok.backend.dto.fairytale.response.FirstFairyTaleResponse;
import com.storysoksok.backend.exception.CustomException;
import com.storysoksok.backend.exception.ErrorCode;
import com.storysoksok.backend.repository.redis.RedisFairyTaleRepository;
import com.storysoksok.backend.service.gpt.GptService;
import com.storysoksok.backend.util.prompt.Prompt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FairyTaleService {
    private final RedisFairyTaleRepository redisFairyTaleRepository;
    private final Prompt prompt;
    private final GptService gptService;

    /**
     * 원하는 동화 중반까지 생성
     * 1. 작성된 프롬프트에 동화책 생성 요청
     * 2. 중반부 동화 내용 RedisHash에 임시 저장
     * 3. 동화내용을 프롬프팅해서 이미지 생성 (이미지 정확도를 올리기 위해서 페이지의 내용당 1번씩 요청. 총 4번 요청)
     * 4. 이미지 RedisHash에 임시 저장 (List)
     * 5. response로 클라이언트에게 전송
     */
    public Object firstFairyTale(FairyTaleCreateRequest request, Member member) {
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

        String firstStory = gptService.generateFirstFairyTale(prompt).orElseThrow(()
                -> {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        });

        List<String> pages = Pattern.compile("(?m)^\\d\\s")  // 1... 2... 3...
                .splitAsStream(firstStory)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());    // [p1, p2, p3, p4]

        List<String> imageList = new ArrayList<>();

        /* 동화 중반부 까지 기준 이미지 생성 시도 */
        for (int i = 1; i < pages.size() + 1; i++) {
            String pageStory = pages.get(i - 1);
            log.debug("페이지 번호 : {}", i);
            log.debug("이야기 : {}", pageStory);

            String imgPrompt = this.prompt.firstFairyTaleImageFormat(pageStory, i);

            String imageUrl = gptService.generatePicture(imgPrompt);
            imageList.add(imageUrl);
        }

        log.debug("이미지 데이터 : {}", imageList);

        redisFairyTaleRepository.save(FirstFairyTale.builder()
                .fairyTaleSubject(map.get("FairyTaleSubject"))
                .fairyTaleLocation(map.get("FairyTaleLocation"))
                .fairyTaleCharacter(map.get("FairyTaleCharacter"))
                .imgList(imageList)
                .firstContent(firstStory)
                .build());

        return FirstFairyTaleResponse.builder()
                .memberId(member.getMemberId())
                .imageUrl(imageList)
                .memberName(member.getName())
                .firstContent(firstStory)
                .build();
    }
}
