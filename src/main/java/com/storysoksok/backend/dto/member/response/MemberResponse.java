package com.storysoksok.backend.dto.member.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class MemberResponse {
    private UUID memberId;
    private String memberName;
    private String email;
    private String role;

    @Builder
    public MemberResponse(UUID memberId, String memberName, String email, String role) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.email = email;
        this.role = role;
    }
}
