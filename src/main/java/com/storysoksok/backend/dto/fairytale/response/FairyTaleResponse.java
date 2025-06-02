package com.storysoksok.backend.dto.fairytale.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class FairyTaleResponse {
    private UUID memberId;
    private UUID fairyTaleId;
    private List<String> storyList;
    private List<String> imageList;

    @Builder
    public FairyTaleResponse(UUID memberId, UUID fairyTaleId, List<String> storyList, List<String> imageList) {
        this.memberId = memberId;
        this.fairyTaleId = fairyTaleId;
        this.storyList = storyList;
        this.imageList = imageList;
    }
}
