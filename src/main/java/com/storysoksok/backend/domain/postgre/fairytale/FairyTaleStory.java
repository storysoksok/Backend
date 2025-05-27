package com.storysoksok.backend.domain.postgre.fairytale;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.storysoksok.backend.domain.postgre.BasePostgresEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FairyTaleStory extends BasePostgresEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID fairyTaleStoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    private FairyTale fairyTale;
    private int pageNum;
    @Column(nullable = false, columnDefinition = "TEXT")
    @Lob
    private String content;
}
