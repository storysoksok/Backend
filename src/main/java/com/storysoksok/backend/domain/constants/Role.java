package com.storysoksok.backend.domain.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ROLE_USER("일반 회원"),
    ROLE_ADMIN("관리자"),
    ROLE_TEST("테스트 회원"),
    ROLE_TEST_ADMIN("테스트 관리자 회원");

    private final String description;
}