package com.storysoksok.backend.repository;

import com.storysoksok.backend.util.oauth.SocialClientRegistration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

/**
 * ClientRegistration 저장소 (InMemory형식으로 저장)
 */
@Configuration
@RequiredArgsConstructor
public class CustomClientRegistrationRepository {

    private final SocialClientRegistration socialClientRegistration;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {

        return new InMemoryClientRegistrationRepository(
                socialClientRegistration.googleClientRegistration()
        );
    }
}