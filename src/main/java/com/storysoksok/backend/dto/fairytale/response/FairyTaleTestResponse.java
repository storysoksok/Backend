package com.storysoksok.backend.dto.fairytale.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class FairyTaleTestResponse {
    private UUID fairyTaleId;
    private String content;
    private String imageUrl;
    private String title;
    private Integer pageNum;
    @Builder
    public FairyTaleTestResponse(UUID fairyTaleId, String content, String imageUrl, String title, Integer pageNum) {
        this.fairyTaleId = fairyTaleId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.title = title;
        this.pageNum = pageNum;
    }
}
