package com.storysoksok.backend.domain.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Correct {
    UNKNOWN("채점 전 퀴즈"),
    CORRECT("정답"),
    UN_CORRECT("오답");
    private final String description;
}
