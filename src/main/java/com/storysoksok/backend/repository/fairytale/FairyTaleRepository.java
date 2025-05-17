package com.storysoksok.backend.repository.fairytale;

import com.storysoksok.backend.domain.postgre.fairytale.FairyTale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FairyTaleRepository extends JpaRepository<FairyTale, UUID> {
}
