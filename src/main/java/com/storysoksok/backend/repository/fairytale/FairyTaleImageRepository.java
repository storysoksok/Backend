package com.storysoksok.backend.repository.fairytale;

import com.storysoksok.backend.domain.postgre.fairytale.FairyTaleImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FairyTaleImageRepository extends JpaRepository<FairyTaleImage, UUID> {
}
