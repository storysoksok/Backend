package com.storysoksok.backend.repository.redis;

import com.storysoksok.backend.domain.redis.MidPartFairyTale;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RedisFairyTaleRepository extends CrudRepository<MidPartFairyTale, UUID> {
}
