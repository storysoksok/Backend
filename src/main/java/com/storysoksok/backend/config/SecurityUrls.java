package com.storysoksok.backend.config;

import java.util.Arrays;
import java.util.List;

/**
 * Security 관련 URL 상수 관리
 */
public class SecurityUrls {

    /**
     * 인증을 생략할 URL 패턴 목록
     */
    public static final List<String> AUTH_WHITELIST = Arrays.asList(
            // API
            "/", // 홈
            "/api/auth/reissue", // accessToken 재발급
            "/api/oauth2/**", // 소셜 로그인
            "/login/**", // 기본 Spring Security OAuth2 로그인경로

            // TEST
            "/test/**", // 개발자용 테스트 api


            // Swagger
            "/docs/**", // Swagger UI
            "/v3/api-docs/**" // Swagger API 문서
    );
}