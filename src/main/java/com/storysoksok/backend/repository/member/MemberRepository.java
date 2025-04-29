package com.storysoksok.backend.repository.member;

import com.storysoksok.backend.domain.postgre.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByUsername(String email);
}
