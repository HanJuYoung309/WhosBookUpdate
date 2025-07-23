package com.example.whosbookupdate.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. 인가(Authorization) 설정: 어떤 요청에 어떤 권한이 필요한가?
                .authorizeHttpRequests(authorize -> authorize
                        // 회원가입 경로는 인증 없이 누구나 접근 가능
                        .requestMatchers("/member/register", "/member/join").permitAll()
                        // 로그인 페이지 자체도 인증 없이 접근 가능하도록 허용
                        .requestMatchers("/login", "/login-error").permitAll()
                        // 정적 리소스 (CSS, JS 등)도 누구나 접근 가능
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()
                        // 루트 경로 및 기타 공개 페이지도 허용
                        .requestMatchers("/", "/home", "/about").permitAll()
                        // 나머지 모든 요청은 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()
                )
                // 2. 폼 로그인(Form Login) 설정
                .formLogin(form -> form
                        .loginPage("/member/login") // 사용자 정의 로그인 페이지 URL 설정
                        .permitAll() // 로그인 관련 페이지는 모두 접근 허용
                )
                // 3. 로그아웃(Logout) 설정
                .logout(logout -> logout
                        .logoutUrl("/perform_logout") // 로그아웃을 처리할 URL (GET/POST 요청 모두 가능)
                        .logoutSuccessUrl("/login?logout") // 로그아웃 성공 시 리다이렉트될 URL
                        .invalidateHttpSession(true) // HTTP 세션 무효화 (기본값: true)
                        .deleteCookies("JSESSIONID") // 삭제할 쿠키 지정 (예: 세션 ID 쿠키)
                        .permitAll() // 로그아웃 관련 페이지는 모두 접근 허용
                ).
                // 4. CSRF (Cross-Site Request Forgery) 보호 설정
                csrf(csrf -> csrf.disable());

        // CSRF 기본값: 모든 POST/PUT/DELETE 요청에 CSRF 토큰 요구 (HTML 폼이나 Ajax 헤더에 포함해야 함)
        // .csrf(Customizer.withDefaults()); // 기본 CSRF 설정 (생략 시 기본값 적용)

        return http.build();
    }





}
