package com.storysoksok.backend.domain.postgre.member;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.storysoksok.backend.domain.constants.Role;
import com.storysoksok.backend.domain.postgre.BasePostgresEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Member extends BasePostgresEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID memberId;

    // 소셜 로그인 시 발급되는 ID
    @Column(nullable = false)
    private String socialLoginId;

    // 이메일
    @Column(unique = true)
    private String username;

    // 이름
    @Column(nullable = false)
    private String name;

    // 권한 (유저, 관리자)
    @Enumerated(EnumType.STRING)
    private Role role;
}