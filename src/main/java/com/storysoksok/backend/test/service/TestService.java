package com.storysoksok.backend.test.service;

import com.storysoksok.backend.domain.postgre.member.Member;
import com.storysoksok.backend.dto.oauth.request.CustomOAuth2User;
import com.storysoksok.backend.dto.oauth.request.LoginRequest;
import com.storysoksok.backend.exception.CustomException;
import com.storysoksok.backend.exception.ErrorCode;
import com.storysoksok.backend.repository.member.MemberRepository;
import com.storysoksok.backend.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

import static com.storysoksok.backend.domain.constants.Role.ROLE_TEST;
import static com.storysoksok.backend.domain.constants.Role.ROLE_TEST_ADMIN;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final Faker koFaker = new Faker(new Locale("ko", "KR"));
    private final Faker enFaker = new Faker(new Locale("en"));

    @Transactional
    public String testSocialLogin(LoginRequest request) {

        log.debug("테스트 계정 로그인을 집행합니다");

        Member member = memberRepository.save(createMockMember(request));
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(member, null);
        String accessToken = jwtUtil.createAccessToken(customOAuth2User);

        log.debug("테스트 로그인 성공: 엑세스 토큰 및 리프레시 토큰 생성");
        log.debug("테스트 accessToken = {}", accessToken);

        return accessToken;
    }

    /**
     * 개발자용 회원 Mock 데이터 생성
     *
     * @param request socialPlatform 네이버/카카오 소셜 로그인 플랫폼
     *                memberType 의로인/대리인
     *                accountStatus 활성화/삭제
     *                isFirstLogin 첫 로그인 여부
     */
    @Transactional
    public Member createMockMember(LoginRequest request) {

        log.debug("테스트 Mock 회원을 생성합니다");
        LocalDate birth = koFaker.timeAndDate().birthday();
        String birthYear = Integer.toString(birth.getYear()); // YYYY
        String birthDay = String.format("%02d%02d", birth.getMonthValue(), birth.getDayOfMonth()); // MMDD

        // 테스트 회원 ROLE 검증
        if (request.getRole() != ROLE_TEST && request.getRole() != ROLE_TEST_ADMIN) {
            log.error("테스트 로그인은 ROLE_TEST, ROLE_TEST_ADMIN 권한만 생성 가능합니다. 요청된 ROLE: {}", request.getRole());
            throw new CustomException(ErrorCode.INVALID_MEMBER_ROLE_REQUEST);
        }

        return Member.builder()
                .socialLoginId(UUID.randomUUID().toString())
                .username(enFaker.internet().emailAddress().replaceAll("[^a-zA-Z0-9@\\s]", ""))
                .name(koFaker.name().name().replaceAll(" ", ""))
                .role(request.getRole())
                .build();
    }
}
