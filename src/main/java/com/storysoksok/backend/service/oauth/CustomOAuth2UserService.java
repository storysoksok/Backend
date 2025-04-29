package com.storysoksok.backend.service.oauth;

import com.storysoksok.backend.domain.constants.Role;
import com.storysoksok.backend.domain.postgre.member.Member;
import com.storysoksok.backend.dto.oauth.request.CustomOAuth2User;
import com.storysoksok.backend.dto.oauth.response.GoogleResponse;
import com.storysoksok.backend.dto.oauth.response.OAuth2Response;
import com.storysoksok.backend.exception.CustomException;
import com.storysoksok.backend.exception.ErrorCode;
import com.storysoksok.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oAuth2User = super.loadUser(request);
        log.debug("요청된 OAuth2User: {}", oAuth2User.getAttributes());

        OAuth2Response oAuth2Response;
        oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        // Member 확인
        Optional<Member> optionalMember = memberRepository.findByUsername(oAuth2Response.getEmail());
        Member member;
        if (optionalMember.isEmpty()) { // 첫 로그인한 회원인 경우
            member = Member.builder()
                    .socialLoginId(oAuth2Response.getProviderId())
                    .username(oAuth2Response.getEmail())
                    .name(oAuth2Response.getName())
                    .role(Role.ROLE_USER)
                    .build();
        } else { // 첫 로그인이 아닌경우
            member = optionalMember.get();
        }
        Member savedMember = memberRepository.save(member);

        return new CustomOAuth2User(savedMember, oAuth2User.getAttributes());
    }

    // 회원 이메일을 통한 CustomOAuth2User 반환
    public CustomOAuth2User loadUserByUsername(String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("회원을 찾을 수 없습니다. 요청 username: {}", username);
                    return new CustomException(ErrorCode.MEMBER_NOT_FOUND);
                });

        return new CustomOAuth2User(member, null);
    }
}