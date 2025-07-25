package com.example.whosbookupdate.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.http.HttpMethod;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ⭐⭐⭐ 진단용: 모든 요청을 허용합니다. 이 설정으로도 Access Denied가 발생하면 다른 문제입니다. ⭐⭐⭐
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // 모든 요청을 허용
                )
                // 2. 폼 로그인(Form Login) 설정
                .formLogin(form -> form
                        .loginPage("/member/login") // 사용자 정의 로그인 페이지 URL (GET 요청)
                        .loginProcessingUrl("/member/login") // 로그인 폼 제출 URL (POST 요청)
                        .defaultSuccessUrl("/", true) // 로그인 성공 시 항상 루트 경로로 리다이렉트
                        .failureUrl("/member/login?error") // 로그인 실패 시 로그인 페이지로 리다이렉트
                        .permitAll() // 로그인 관련 페이지는 모두 접근 허용
                )
                // 3. 로그아웃(Logout) 설정
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃을 처리할 URL (index.html과 일치)
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 리다이렉트될 URL
                        .invalidateHttpSession(true) // HTTP 세션 무효화 (기본값: true)
                        .deleteCookies("JSESSIONID") // 삭제할 쿠키 지정 (예: 세션 ID 쿠키)
                )
                // 4. CSRF (Cross-Site Request Forgery) 보호 설정
                // 개발 및 디버깅 목적으로 임시 비활성화.
                // 문제가 해결되면 반드시 활성화하고 HTML에 CSRF 토큰을 올바르게 포함해야 합니다.
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManage(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}