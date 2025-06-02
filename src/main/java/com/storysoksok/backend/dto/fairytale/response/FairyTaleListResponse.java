package com.storysoksok.backend.dto.fairytale.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class FairyTaleListResponse {
    private UUID memberId;  // 회원 PK
    private UUID fairyTaleId;  // 동화책 완성본 PK
    private String title;  // 제목
    @JsonFormat
    private LocalDateTime createAt;  // 완성된 날짜
    private String firstImageUrl;  // 첫 페이지 이미지

    @Builder
    public FairyTaleListResponse(UUID memberId, UUID fairyTaleId, String title, LocalDateTime createAt, String firstImageUrl) {
        this.memberId = memberId;
        this.fairyTaleId = fairyTaleId;
        this.title = title;
        this.createAt = createAt;
        this.firstImageUrl = firstImageUrl;
    }
}
