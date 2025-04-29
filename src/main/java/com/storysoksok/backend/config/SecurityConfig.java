package com.storysoksok.backend.config;

import com.storysoksok.backend.repository.CustomClientRegistrationRepository;
import com.storysoksok.backend.service.oauth.CustomOAuth2UserService;
import com.storysoksok.backend.util.filter.CustomLogoutHandler;
import com.storysoksok.backend.util.filter.CustomSuccessHandler;
import com.storysoksok.backend.util.filter.TokenAuthenticationFilter;
import com.storysoksok.backend.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final CustomSuccessHandler customSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomLogoutHandler customLogoutHandler;
    private final CustomClientRegistrationRepository customClientRegistrationRepository;

    /**
     * 허용된 CORS Origin 목록
     */
    private static final String[] ALLOWED_ORIGINS = {

            // TODO : 프론트 배포 경로 추가, 서버 배포 주소(도메인) 추가

            // Local
            "http://localhost:8080", // 로컬 API 서버
            "http://localhost:3000" // 로컬 웹 서버
    };

    /**
     * Security Filter Chain 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SecurityUrls.AUTH_WHITELIST.toArray(new String[0]))
                        .permitAll() // AUTH_WHITELIST에 등록된 URL은 인증 허용
                        .anyRequest().authenticated()
                )
                // 로그아웃
                .logout(logout -> logout
                        .logoutUrl("/logout") // "/logout" 경로로 접근 시 로그아웃
                        .addLogoutHandler(customLogoutHandler) // 로그아웃 핸들러 등록 (쿠키 삭제, 블랙리스트)
                        .logoutSuccessUrl("/login") // 로그아웃 성공 후 로그인 창 이동
                        .invalidateHttpSession(true)
                )
                // 세션 설정: STATELESS
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // OAuth2
                .oauth2Login(oauth2 -> oauth2
                        .clientRegistrationRepository(customClientRegistrationRepository.clientRegistrationRepository())
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig
                                        .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler))
                // JWT Filter
                .addFilterAfter(
                        new TokenAuthenticationFilter(jwtUtil),
                        OAuth2LoginAuthenticationFilter.class
                )
                .build();
    }

    /**
     * 인증 메니저 설정
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration)
            throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * CORS 설정 소스 빈
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(ALLOWED_ORIGINS));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
        return urlBasedCorsConfigurationSource;
    }
}