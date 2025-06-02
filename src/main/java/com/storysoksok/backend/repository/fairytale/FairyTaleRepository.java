package com.storysoksok.backend.repository.fairytale;

import com.storysoksok.backend.domain.postgre.fairytale.FairyTale;
import com.storysoksok.backend.domain.postgre.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FairyTaleRepository extends JpaRepository<FairyTale, UUID> {
    List<FairyTale> findAllByMember(Member member);
}
