package com.storysoksok.backend.repository.fairytale;

import com.storysoksok.backend.domain.postgre.fairytale.FairyTale;
import com.storysoksok.backend.domain.postgre.fairytale.FairyTaleImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FairyTaleImageRepository extends JpaRepository<FairyTaleImage, UUID> {
    FairyTaleImage findByFairyTaleAndPageNum(FairyTale fairyTale, Integer pageNum);

    List<FairyTaleImage> findAllByFairyTale(FairyTale fairyTale);
}
