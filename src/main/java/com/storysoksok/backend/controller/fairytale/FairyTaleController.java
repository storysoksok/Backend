package com.storysoksok.backend.controller.fairytale;

import com.storysoksok.backend.controller.fairytale.docs.FairyTaleControllerDocs;
import com.storysoksok.backend.domain.postgre.member.Member;
import com.storysoksok.backend.dto.fairytale.request.FairyTaleCreateRequest;
import com.storysoksok.backend.dto.fairytale.request.SecondHalfFairyTaleRequest;
import com.storysoksok.backend.dto.fairytale.response.*;
import com.storysoksok.backend.dto.oauth.request.CustomOAuth2User;
import com.storysoksok.backend.service.fairytale.FairyTaleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(
        name = "동화 생성용 API",
        description = "동화 관련 API 제공"
)
public class FairyTaleController implements FairyTaleControllerDocs {
    private final FairyTaleService firstFairyTale;

    @Override
    @PostMapping("/fairy-tale/first")
    public ResponseEntity<FirstFairyTaleResponse> firstFairyTale(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                 @RequestBody @Valid FairyTaleCreateRequest request) {
        Member member = customOAuth2User.getMember();
        return ResponseEntity.ok(firstFairyTale.firstFairyTale(request,member));
    }

    @Override
    @PostMapping("/fairy-tale/{fairy-tale}/{page-num}")
    public ResponseEntity<FairyTaleImageResponse> createFairyTaleImage(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                       @PathVariable(value = "fairy-tale") UUID fairyTaleId,
                                                                       @PathVariable(value = "page-num") Integer pageNum) {

        Member member = customOAuth2User.getMember();
        return ResponseEntity.ok(firstFairyTale.createFairyTaleImage(member, fairyTaleId, pageNum));
    }

    @Override
    @PostMapping("/fairy-tale/second-half")
    public ResponseEntity<SecondHalfFairyTaleResponse> secondHalfRecommendStory(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                                @RequestBody @Valid SecondHalfFairyTaleRequest request) {
        Member member = customOAuth2User.getMember();
        return ResponseEntity.ok(firstFairyTale.secondHalfFairyTale(request,member));
    }

    @Override
    @GetMapping("/fairy-tale/test/{page-num}")
    public ResponseEntity<FairyTaleTestResponse> getFairyTaleTest(@PathVariable(value = "page-num") Integer pageNum) {
        return ResponseEntity.ok(firstFairyTale.getFairyTaleTest(pageNum));
    }

    @Override
    @GetMapping("/fairy-tale")
    public ResponseEntity<List<FairyTaleListResponse>> getFairyTaleList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Member member = customOAuth2User.getMember();
        return ResponseEntity.ok(firstFairyTale.getFairyTaleList(member));
    }

    @Override
    @GetMapping("/fairy-tale/{fairy-tale-id}")
    public ResponseEntity<FairyTaleResponse> getFairyTale(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                          @PathVariable(value = "fairy-tale-id") UUID id) {
        Member member = customOAuth2User.getMember();
        return ResponseEntity.ok(firstFairyTale.getFairyTale(member, id));
    }
}
