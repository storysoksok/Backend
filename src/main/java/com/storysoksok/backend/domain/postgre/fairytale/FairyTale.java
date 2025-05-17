package com.storysoksok.backend.domain.postgre.fairytale;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.storysoksok.backend.domain.postgre.BasePostgresEntity;
import com.storysoksok.backend.domain.postgre.member.Member;
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
public class FairyTale extends BasePostgresEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID fairyTaleId;
    @Lob
    @Column
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    private String fairyTaleSubject;  // 동화주제
    private String fairyTaleCharacter;  // 동화 캐릭터
    private String fairyTaleLocation;  // 동화장소
}
