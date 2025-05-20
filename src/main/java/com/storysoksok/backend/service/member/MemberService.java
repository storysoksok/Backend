package com.storysoksok.backend.service.member;

import com.storysoksok.backend.domain.postgre.member.Member;
import com.storysoksok.backend.dto.member.response.MemberResponse;
import com.storysoksok.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse getMemberInfo(Member member) {
        return MemberResponse.builder()
                .memberName(member.getName())
                .memberId(member.getMemberId())
                .role(member.getRole().name())
                .email(member.getUsername())
                .build();
    }
}
