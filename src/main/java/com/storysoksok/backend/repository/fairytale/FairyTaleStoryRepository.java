package com.storysoksok.backend.repository.fairytale;

import com.storysoksok.backend.domain.postgre.fairytale.FairyTale;
import com.storysoksok.backend.domain.postgre.fairytale.FairyTaleStory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FairyTaleStoryRepository extends JpaRepository<FairyTaleStory, UUID> {
    List<FairyTaleStory> findAllByFairyTaleOrderByPageNumAsc(FairyTale fairyTale);

    FairyTaleStory findByFairyTaleAndPageNum(FairyTale fairyTale, Integer pageNum);
}
