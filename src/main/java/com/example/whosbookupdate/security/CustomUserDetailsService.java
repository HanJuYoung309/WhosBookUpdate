package com.example.whosbookupdate.security;

import com.example.whosbookupdate.domain.MemberVO;
import com.example.whosbookupdate.mapper.MemberMapper; // MemberMapper 임포트 추가
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // private final MemberRepository memberRepository; // MemberRepository 대신 MemberMapper 사용
    private final MemberMapper memberMapper; // MemberMapper 주입으로 변경

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername: 사용자 이메일 = " + username);

        // MemberMapper를 사용하여 사용자 정보를 불러오는 형식으로 변경합니다.
        // memberMapper.selectByEmail(username)이 null을 반환할 수 있으므로 명시적으로 null 체크를 추가합니다.
        MemberVO member = memberMapper.findByEmail(username);

        if (member == null) {
            log.warn("User not found with email: " + username);
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        // ⭐ member가 null이 아님을 확신할 수 있는 지점
        // 이 로그는 member가 null이 아닐 때만 실행됩니다.
        log.info("사용자 로드 성공: 이메일 = " + member.getEmail() + ", 해시된 비밀번호 = " + member.getPassword());

        // UserDetails 객체 반환
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                // 권한이 있다면 여기에 추가 (예: .authorities("ROLE_USER"))
                .authorities(Collections.emptyList())
                .build();
    }
}