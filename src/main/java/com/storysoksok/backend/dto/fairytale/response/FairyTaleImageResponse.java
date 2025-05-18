package com.storysoksok.backend.dto.fairytale.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class FairyTaleImageResponse {
    private String imageUrl;
    private UUID memberId;
    private UUID midPartFairyTaleId;
    private String memberName;
    private Integer pageNum;

    @Builder
    public FairyTaleImageResponse(String imageUrl, UUID memberId, UUID midPartFairyTaleId, String memberName, Integer pageNum) {
        this.imageUrl = imageUrl;
        this.memberId = memberId;
        this.midPartFairyTaleId = midPartFairyTaleId;
        this.memberName = memberName;
        this.pageNum = pageNum;
    }
}
