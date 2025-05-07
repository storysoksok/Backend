package com.storysoksok.backend.test.controller;

import com.storysoksok.backend.dto.oauth.request.LoginRequest;
import com.storysoksok.backend.test.controller.docs.TestControllerDocs;
import com.storysoksok.backend.test.service.TestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Tag(
        name = "개발자 테스트용 API",
        description = "개발자 편의를 위한 테스트용 API 제공"
)
public class TestController implements TestControllerDocs {
    private final TestService testService;

    @Override
    @PostMapping(value = "/login", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> socialLogin(@ModelAttribute LoginRequest request) {
        return ResponseEntity.ok(testService.testSocialLogin(request));
    }
}
