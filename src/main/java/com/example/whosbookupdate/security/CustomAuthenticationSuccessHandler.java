package com.example.whosbookupdate.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler  implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 세션 생성 강제
        HttpSession session = request.getSession(true);

        System.out.println("로그인 성공 - SessionId: " + session.getId());
        System.out.println("인증 정보: " + authentication.getName());
        System.out.println("권한: " + authentication.getAuthorities());

        // 로그인 성공 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\": true, \"message\": \"로그인 성공\"}");
    }

}
