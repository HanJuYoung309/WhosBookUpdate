package com.example.whosbookupdate.security;

import com.example.whosbookupdate.domain.MemberVO;
import com.example.whosbookupdate.mapper.MemberMapper; // MemberMapper 임포트 추가
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

// CustomUserDetailsService.java

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberVO member = memberMapper.findByEmail(username);

        if (member == null) {
            throw new UsernameNotFoundException("Member with email " + username + " not found");
        }

        // --- 로그 추가 ---
        log.info("DB에서 조회된 Member ID: {}", member.getMemberId());

        if (member.getMemberId() == null) {
            log.error("치명적 오류: Member ID가 null입니다. DB나 매퍼를 확인하세요.");
            // ID가 null이면 인증 실패로 처리해야 함
            throw new UsernameNotFoundException("User ID is null for email: " + username);
        }
        // --- 로그 추가 끝 ---

        return new User(
                String.valueOf(member.getMemberId()),
                member.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // 권한 추가 권장
        );
    }
}