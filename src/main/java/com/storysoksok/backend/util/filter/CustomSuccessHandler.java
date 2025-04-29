package com.storysoksok.backend.util.filter;

import com.storysoksok.backend.dto.oauth.request.CustomOAuth2User;
import com.storysoksok.backend.exception.CustomException;
import com.storysoksok.backend.exception.ErrorCode;
import com.storysoksok.backend.util.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String REFRESH_PREFIX = "RT:";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        // CustomOAuth2User
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String accessToken = jwtUtil.createAccessToken(customOAuth2User);
        String refreshToken = jwtUtil.createRefreshToken(customOAuth2User);

        log.debug("로그인 성공: 엑세스 토큰 및 리프레시 토큰 생성");
        log.debug("accessToken = {}", accessToken);
        log.debug("refreshToken = {}", refreshToken);

        // RefreshToken을 Redisd에 저장 (key: RT:memberId)
        redisTemplate.opsForValue().set(
                REFRESH_PREFIX + customOAuth2User.getMemberId(),
                refreshToken,
                jwtUtil.getRefreshExpirationTime(),
                TimeUnit.MILLISECONDS
        );

        // 로그인 쿼리 파라미터 Redirect URI 확인
        String redirectUri = request.getParameter("redirectUri");
        if (redirectUri == null) {
            log.debug("로그인 시 요청된 Redirect URI가 없으므로 기본 경로로 설정합니다.");
            redirectUri = "http://localhost:3000";  // TODO 프론트엔드 메인페이지 주소
        }
        response.addHeader("Authorization", "Bearer " + accessToken);

        // 쿠키에 refreshToken 추가
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true); // HttpOnly 설정
        cookie.setSecure(true);
        cookie.setPath("/");
//        cookie.setDomain("");
        cookie.setMaxAge((int) (jwtUtil.getRefreshExpirationTime() / 1000)); // 쿠키 maxAge는 초 단위 이므로, 밀리초를 1000으로 나눔
        response.addCookie(cookie);

        // 로그인 성공 후 메인 페이지로 리다이렉트
        try {
            log.debug("로그인 성공, 메인페이지로 리다이렉트 됩니다");
            if (!response.isCommitted()) {
                response.sendRedirect(redirectUri);
            }
        } catch (IOException e) {
            log.error("로그인 성공 후 리다이렉트 과정에서 문제가 발생했습니다. {}", e.getMessage());
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}