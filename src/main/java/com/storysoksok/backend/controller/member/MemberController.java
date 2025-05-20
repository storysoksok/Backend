package com.storysoksok.backend.controller.member;

import com.storysoksok.backend.controller.member.docs.MemberControllerDocs;
import com.storysoksok.backend.domain.postgre.member.Member;
import com.storysoksok.backend.dto.member.response.MemberResponse;
import com.storysoksok.backend.dto.oauth.request.CustomOAuth2User;
import com.storysoksok.backend.service.member.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(
        name = "회원 관련 API",
        description = "회원 관련 API 제공"
)
public class MemberController implements MemberControllerDocs {
    private final MemberService memberService;

    @Override
    @GetMapping("/member")
    public ResponseEntity<MemberResponse> getMemberInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Member member = customOAuth2User.getMember();
        return ResponseEntity.ok(memberService.getMemberInfo(member));
    }
}
