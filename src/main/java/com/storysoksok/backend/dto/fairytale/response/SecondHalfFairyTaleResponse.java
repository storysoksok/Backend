package com.storysoksok.backend.dto.fairytale.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class SecondHalfFairyTaleResponse {
    private UUID memberId;
    private UUID secondHalfFairyTaleId;
    private String memberName;
    private Integer pageNumber;
    private List<String> secondHalfFairyTaleStory;
    private String fairyTaleTitle;
    private String imageUrl;

    @Builder
    public SecondHalfFairyTaleResponse(UUID memberId, UUID secondHalfFairyTaleId, String memberName, Integer pageNumber, List<String> secondHalfFairyTaleStory, String fairyTaleTitle, String imageUrl) {
        this.memberId = memberId;
        this.secondHalfFairyTaleId = secondHalfFairyTaleId;
        this.memberName = memberName;
        this.pageNumber = pageNumber;
        this.secondHalfFairyTaleStory = secondHalfFairyTaleStory;
        this.fairyTaleTitle = fairyTaleTitle;
        this.imageUrl = imageUrl;
    }
}
