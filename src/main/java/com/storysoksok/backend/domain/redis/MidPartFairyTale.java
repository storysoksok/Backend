package com.storysoksok.backend.domain.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@RedisHash("midPartFairyTaleId")
public class MidPartFairyTale {
    @Id
    private UUID id;
    private List<String> imgList;  // 페이지당 동화 삽화
    private List<String> pageStory;  // 페이지당 동화 내용
    private List<String> secondHalfRecommendStory;  // 후반부 이야기 추천 리스트
    private UUID memberId;
    private String fairyTaleSubject;  // 동화주제
    private String fairyTaleCharacter;  // 동화 캐릭터
    private String fairyTaleLocation;  // 동화장소


    public void addImage(String imageUrl) {
        this.imgList.add(imageUrl);
    }
    @Builder
    public MidPartFairyTale(UUID id, List<String> imgList, List<String> pageStory, List<String> secondHalfRecommendStory, UUID memberId, String fairyTaleSubject, String fairyTaleCharacter, String fairyTaleLocation) {
        this.id = id;
        this.imgList = imgList;
        this.pageStory = pageStory;
        this.secondHalfRecommendStory = secondHalfRecommendStory;
        this.memberId = memberId;
        this.fairyTaleSubject = fairyTaleSubject;
        this.fairyTaleCharacter = fairyTaleCharacter;
        this.fairyTaleLocation = fairyTaleLocation;
    }
}
