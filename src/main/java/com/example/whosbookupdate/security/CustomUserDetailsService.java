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

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername: 사용자 이메일 = " + username);

        MemberVO member = memberMapper.findByEmail(username);

        if (member == null) {
            log.warn("User not found with email: " + username);
            throw new UsernameNotFoundException("User not found with email: " + username);
        }


        log.info("사용자 로드 성공: 이메일 = " + member.getEmail() + ", 해시된 비밀번호 = " + member.getPassword());

        // UserDetails 객체 반환
        return new CustomUserDetails(member);
    }
}