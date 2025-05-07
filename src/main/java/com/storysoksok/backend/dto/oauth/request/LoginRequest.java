package com.storysoksok.backend.dto.oauth.request;

import com.storysoksok.backend.domain.constants.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
    @Schema(defaultValue = "ROLE_TEST")
    private Role role;

    @Builder
    public LoginRequest(Role role) {
        this.role = role;
    }
}
