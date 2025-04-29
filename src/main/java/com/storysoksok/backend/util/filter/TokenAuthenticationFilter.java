package com.storysoksok.backend.util.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storysoksok.backend.config.SecurityUrls;
import com.storysoksok.backend.exception.ErrorCode;
import com.storysoksok.backend.exception.ErrorResponse;
import com.storysoksok.backend.util.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 토큰 기반 인증 필터
 */
@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        log.debug("요청된 URI: {}", uri);

        // 화이트리스트 체크 : 화이트리스트 경로면 필터링 건너뜀
        if (isWhitelistedPath(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 요청 타입 구분 : API 요청/관리자 페이지 요청
        boolean isApiRequest = uri.startsWith("/api/");
        boolean isAdminPageRequest = uri.startsWith("/admin/");

        try {
            String token = null;
            String bearerToken = request.getHeader("Authorization");
            // 토큰 추출: 요청 타입에 따라 헤더 또는 파라미터에서 토큰 추출
            if (isApiRequest) {
                log.debug("일반 API 요청입니다.");
                // API 요청 : Authorization 헤더에서 "Bearer " 토큰 추출
                if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                    token = bearerToken.substring(7).trim(); // "Bearer " 제거
                }
            } else if (isAdminPageRequest) {
                log.debug("관리자 페이지 요청입니다.");
                // 관리자 페이지 요청: Authorization 헤더 우선 확인
                if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                    token = bearerToken.substring(7).trim();
                } else {
                    // Authorization 헤더에 토큰이 없는 경우 파라미터 확인
                    String paramToken = request.getParameter("accessToken");
                    if (paramToken != null && !paramToken.isEmpty()) {
                        token = paramToken;
                    }
                }
            }

            // 토큰 검증: 토큰이 유효하면 인증 설정
            if (token != null && jwtUtil.validateToken(token)) {
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 관리자 페이지 접근 권한 체크: 관리자 권한 없으면 로그인 페이지로 리다이렉트 TODO: 추후 테스트계정 권한 삭제
                if (isAdminPageRequest && !hasAdminRole(authentication) && !hasTestAdminRole(authentication)) {
                    log.error("관리자 권한이 없습니다. 로그인페이지로 리다이렉트합니다.");
                    response.sendRedirect("/login");
                    return;
                }

                // 인증 성공
                filterChain.doFilter(request, response);
                return;
            }

            // 토큰이 없거나 유효하지 않은 경우
            if (isApiRequest) {
                // 토큰 없음
                if (token == null) {
                    log.error("토큰이 존재하지 않습니다.");
                    sendErrorResponse(response, ErrorCode.MISSING_AUTH_TOKEN);
                } else { // 유효하지 않은 토큰
                    log.error("토큰이 유효하지 않습니다.");
                    sendErrorResponse(response, ErrorCode.INVALID_ACCESS_TOKEN);
                }
                return; // 필터 체인 진행하지 않음
            } else if (isAdminPageRequest) {
                // 관리자 페이지: 로그인 페이지로 리다이렉트
                log.error("관리자 페이지 요청 시, 토큰이 없거나 유효하지 않습니다.");
                response.sendRedirect("/login");
                return;
            }
        } catch (ExpiredJwtException e) {
            log.error("토큰 만료: {}", e.getMessage());
            // 토큰 만료 예외 처리
            if (isApiRequest) {
                sendErrorResponse(response, ErrorCode.EXPIRED_ACCESS_TOKEN);
            } else {
                response.sendRedirect("/login");
            }
            return;
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }

    /**
     * 에러 응답을 JSON 형태로 클라이언트에 전송
     *
     * @param response  HttpServletResponse 객체
     * @param errorCode 발생한 에러코드
     * @throws IOException
     */
    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode.getStatus().value());
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(errorCode, errorCode.getMessage());

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), errorResponse);
    }

    /**
     * 화이트리스트 경로 확인 (인증x)
     *
     * @param uri 요청된 URI
     * @return 화이트리스트 여부
     */
    private boolean isWhitelistedPath(String uri) {
        return SecurityUrls.AUTH_WHITELIST.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }

    /**
     * 관리자 권한 확인
     *
     * @param authentication 인증 정보
     * @return 관리자 권한 여부
     */
    private boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    /**
     * 테스트 계정 확인
     * TODO: 추후 삭제
     */
    private boolean hasTestAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEST_ADMIN"));
    }
}