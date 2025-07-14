package com.example.whosbookupdate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration; // CorsConfiguration 임포트
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // UrlBasedCorsConfigurationSource 임포트
import org.springframework.web.filter.CorsFilter; // CorsFilter 임포트

@Configuration
@EnableWebSecurity // 스프링 시큐리티 활성화
public class SecurityConfig {

    // PasswordEncoder 빈 등록 (비밀번호 암호화를 위해 필요)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt 해싱 알고리즘 사용
    }

    // Spring Security 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF (Cross-Site Request Forgery) 보호 비활성화
                // REST API에서는 일반적으로 토큰 기반 인증을 사용하므로 CSRF를 비활성화합니다.
                // 개발 편의상 비활성화하지만, 운영 시에는 적절한 CSRF 보호를 고려해야 합니다.
                .csrf(csrf -> csrf.disable()) // Spring Security 6.x 문법

                // 2. CORS (Cross-Origin Resource Sharing) 설정 활성화
                // CorsConfig.java에서 설정한 CORS 정책을 적용합니다.
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Spring Security 6.x 문법

                // 3. 요청에 대한 인가(Authorization) 규칙 설정
                .authorizeHttpRequests(authorize -> authorize
                        // "/member/login"과 "/member/join" 경로는 인증 없이 누구나 접근 허용 (permitAll)
                        .requestMatchers("/member/login", "/member/join").permitAll()
                        // H2-console 사용 시 필요 (개발용)
                        // .requestMatchers("/h2-console/**").permitAll()
                        // 그 외 모든 요청은 인증 필요 (authenticated)
                        .anyRequest().authenticated()
                )
                // 4. 폼 로그인 비활성화 (REST API이므로 기본 폼 로그인 페이지를 사용하지 않습니다.)
                .formLogin(form -> form.disable()) // Spring Security 6.x 문법

                // 5. HTTP Basic 인증 비활성화 (REST API이므로 기본 HTTP Basic 인증을 사용하지 않습니다.)
                .httpBasic(basic -> basic.disable()); // Spring Security 6.x 문법

        return http.build();
    }

    // CORS 전역 설정 (WebMvcConfigurer 대신 SecurityConfig 내에서 정의)
    // 이렇게 하면 Spring Security의 CORS 필터가 먼저 적용됩니다.
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Vue.js 애플리케이션의 출처를 정확히 명시합니다.
        configuration.addAllowedOrigin("http://localhost:8082");
        // 허용할 HTTP 메서드
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE, OPTIONS 등 모든 메서드 허용
        // 허용할 헤더
        configuration.addAllowedHeader("*"); // 모든 헤더 허용
        // 자격 증명(쿠키, HTTP 인증) 허용
        configuration.setAllowCredentials(true);
        // Preflight 요청 결과를 캐시할 시간 (초)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 CORS 설정 적용
        return source;
    }
}