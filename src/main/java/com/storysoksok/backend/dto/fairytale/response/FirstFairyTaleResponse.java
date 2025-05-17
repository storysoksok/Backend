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
    private String memberName;
    private String firstContent;
    private List<String> imageUrl;
    @Builder
    public FirstFairyTaleResponse(UUID memberId, String memberName, String firstContent, List<String> imageUrl) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.firstContent = firstContent;
        this.imageUrl = imageUrl;
    }
}
