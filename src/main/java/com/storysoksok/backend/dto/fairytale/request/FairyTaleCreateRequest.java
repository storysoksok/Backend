package com.storysoksok.backend.dto.fairytale.request;

import com.storysoksok.backend.domain.constants.FairyTaleCharacter;
import com.storysoksok.backend.domain.constants.FairyTaleLocation;
import com.storysoksok.backend.domain.constants.FairyTaleSubject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FairyTaleCreateRequest {
    @Schema(defaultValue = "DOG")
    @NotNull(message = "동화책 등장인물 등록은 필수입니다.")
    private FairyTaleCharacter fairyTaleCharacter;
    @Schema(defaultValue = "USING_MAGIC")
    @NotNull(message = "동화책 주제 등록은 필수입니다.")
    private FairyTaleSubject fairyTaleSubject;
    @Schema(defaultValue = "SKY")
    @NotNull(message = "동화책 장소 등록은 필수입니다.")
    private FairyTaleLocation fairyTaleLocation;
    private String otherCharacter;  // 캐릭터가 기타일 경우 받아올 텍스트
    private String otherSubject;  // 주제가 기타일 경우 받아올 텍스트
    private String otherLocation;  // 장소가 기타일 경우 받아올 텍스트
}
