package com.storysoksok.backend.dto.fairytale.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class FirstFairyTaleResponse {
    private UUID memberId;
    private UUID midPartFairyTaleId;
    private String memberName;
    private Integer pageNumber;
    private List<String> midPartFairyTaleStory;
    private String imageUrl;
    @Builder
    public FirstFairyTaleResponse(UUID memberId, UUID midPartFairyTaleId, String memberName, Integer pageNumber, List<String> midPartFairyTaleStory, String imageUrl) {
        this.memberId = memberId;
        this.midPartFairyTaleId = midPartFairyTaleId;
        this.memberName = memberName;
        this.pageNumber = pageNumber;
        this.midPartFairyTaleStory = midPartFairyTaleStory;
        this.imageUrl = imageUrl;
    }
}
