package com.storysoksok.backend.dto.oauth.request;

import com.storysoksok.backend.domain.postgre.member.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final Member member;
    private final Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(member.getRole().name()));
    }

    @Override
    public String getName() {
        return member.getName(); // 이름
    }

    public String getUsername() {
        return member.getUsername(); // 이메일
    }

    public boolean isCredentialsNonExpired() {
        return true; // 인증 정보 항상 유효
    }

    public String getMemberId() {
        return member.getMemberId().toString(); // 회원의 memberId (UUID)를 string 으로 반환
    }
}