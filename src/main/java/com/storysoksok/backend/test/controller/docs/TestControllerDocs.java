package com.storysoksok.backend.test.controller.docs;


import com.storysoksok.backend.dto.oauth.request.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;

public interface TestControllerDocs {

    @Operation(
            summary = "테스트 로그인",
            description = """
                    
                    이 API는 인증이 필요하지 않습니다.

                    ### 요청 파라미터
                    - **role** (String): 회원 권한 [필수]

                    ### 유의사항
                    - 개발자의 편의를 위한 소셜 로그인 회원가입/로그인 메서드입니다
                    - 스웨거에서 테스트 용도로만 사용해야하며, 엑세스 토큰만 제공됩니다.
                    - `ROLE_TEST`, `ROLE_TEST_ADMIN`만 선택 가능합니다

                    """
    )
    ResponseEntity<String> socialLogin(LoginRequest request);
}
