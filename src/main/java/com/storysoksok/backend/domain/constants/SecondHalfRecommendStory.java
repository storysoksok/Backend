package com.storysoksok.backend.domain.constants;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SecondHalfRecommendStory {
    FIRST_HALF_RECOMMEND_STORY("첫 선택지 선택"),

    SECOND_HALF_RECOMMEND_STORY("두번째 선택지 선택"),

    THIRD_HALF_RECOMMEND_STORY("세번째 선택지 선택"),

    ETC("직접 사용자가 입력한 내용");

    private final String description;
}
