package com.storysoksok.backend.domain.postgre.fairytale;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class FairyTaleImage extends BasePostgresEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID fairyImgId;
    @Column(columnDefinition = "TEXT", length = 1024)
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    private FairyTale fairyTale;
}
