package com.storysoksok.backend.controller.member.docs;

import com.storysoksok.backend.dto.member.response.MemberResponse;
import com.storysoksok.backend.dto.oauth.request.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;

public interface MemberControllerDocs {

    @Operation(
            summary = "회원 정보 조회",
            description = """
                    
                    이 API는 인증이 필요합니다.

                    ### 유의사항
                    - 엑세스 토큰을 받아와 해당 회원의 정보를 조회하는 API 입니다
                    - 회원 정보가 필요할 시 해당 API를 호출하면 됩니다. 
                    """
    )
    ResponseEntity<MemberResponse> getMemberInfo(CustomOAuth2User customOAuth2User);
}
