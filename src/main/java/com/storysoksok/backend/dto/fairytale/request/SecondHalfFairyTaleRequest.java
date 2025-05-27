package com.storysoksok.backend.dto.fairytale.request;

import com.storysoksok.backend.domain.constants.SecondHalfRecommendStory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class SecondHalfFairyTaleRequest {
    @NotNull(message = "선택지 입력은 필수입니다.")
    @Schema(defaultValue = "SECOND_HALF_RECOMMEND_STORY")
    private SecondHalfRecommendStory secondHalfRecommendStory;

    @NotNull(message = "중반부 동화의 PK값은 필수입니다.")
    private UUID midPartFairyTaleId;

    private String otherRecommendStory;
}
