package com.storysoksok.backend.domain.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@RedisHash("firstFairyTale")
public class FirstFairyTale {
    @Id
    private UUID id;
    private List<String> imgList;
    private String firstContent;
    private UUID memberId;
    private String fairyTaleSubject;  // 동화주제
    private String fairyTaleCharacter;  // 동화 캐릭터
    private String fairyTaleLocation;  // 동화장소
    @Builder
    public FirstFairyTale(List<String> imgList, String firstContent, UUID memberId, String fairyTaleSubject, String fairyTaleCharacter, String fairyTaleLocation) {
        this.imgList = imgList;
        this.firstContent = firstContent;
        this.memberId = memberId;
        this.fairyTaleSubject = fairyTaleSubject;
        this.fairyTaleCharacter = fairyTaleCharacter;
        this.fairyTaleLocation = fairyTaleLocation;
    }
}
