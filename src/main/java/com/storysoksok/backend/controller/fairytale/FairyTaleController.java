package com.storysoksok.backend.controller.fairytale;

import com.storysoksok.backend.controller.fairytale.docs.FairyTaleControllerDocs;
import com.storysoksok.backend.domain.postgre.member.Member;
import com.storysoksok.backend.dto.fairytale.request.FairyTaleCreateRequest;
import com.storysoksok.backend.dto.oauth.request.CustomOAuth2User;
import com.storysoksok.backend.service.fairytale.FairyTaleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(
        name = "동화 생성용 API",
        description = "AI를 이용한 동화생성 관련 API 제공"
)
public class FairyTaleController implements FairyTaleControllerDocs {
    private final FairyTaleService firstFairyTale;

    @Override
    @PostMapping("/fairy-tale/first")
    public ResponseEntity<Object> firstFairyTale(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                 @RequestBody FairyTaleCreateRequest request) {
        Member member = customOAuth2User.getMember();
        return ResponseEntity.ok(firstFairyTale.firstFairyTale(request,member));
    }
}
