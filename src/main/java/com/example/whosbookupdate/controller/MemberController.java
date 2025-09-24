package com.example.whosbookupdate.controller;


import com.example.whosbookupdate.domain.MemberVO;
import com.example.whosbookupdate.dto.LoginRequest;
import com.example.whosbookupdate.dto.MemberInfoDTO;
import com.example.whosbookupdate.dto.MemberRegistrationDto;
import com.example.whosbookupdate.dto.MemberResponseDto;
import com.example.whosbookupdate.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.util.Map;


@Log4j2
@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    private final AuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository;

    @Autowired
    public MemberController(MemberService memberService, AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.memberService = memberService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    //회원가입 처리
    @PostMapping("/signup")
    public ResponseEntity<?> memberPOST( @RequestBody  MemberRegistrationDto memberRegistrationDto) {

        try{
            MemberVO newMemberVO = memberService.join(memberRegistrationDto);

            return new ResponseEntity<>(newMemberVO, HttpStatus.CREATED);
        }catch (DuplicateKeyException e){
            return new ResponseEntity<>("이미 등록된 이메일입니다",HttpStatus.BAD_REQUEST);
        }catch (RuntimeException e){
            System.err.println("회원가입 API 오류"+e.getMessage());
            return new ResponseEntity<>("회원가입 처리중 오류가 발생했습니다.",HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e) {
            // 예상치 못한 모든 예외 처리
            System.err.println("예상치 못한 회원가입 API 오류: " + e.getMessage());
            return new ResponseEntity<>("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/me")
    public MemberInfoDTO getMyInfo() {
        // 현재 인증 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증되지 않았거나, Principal이 null인 경우 빈 DTO를 반환
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            log.info("사용자 인증 정보 없음. 빈 DTO 반환.");
            return new MemberInfoDTO();
        }

        // 로그인된 사용자의 Principal 객체에서 memberId를 추출합니다.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String memberIdStr = userDetails.getUsername(); // CustomUserDetailsService에서 memberId를 username으로 설정함

        // memberId를 Long 타입으로 변환
        Long memberId=1L;
        try {
            memberId = Long.parseLong(memberIdStr);
        } catch (NumberFormatException e) {
            log.error("Member ID 변환 실패: {}", memberIdStr, e);
            return new MemberInfoDTO();
        }

        // 서비스 레이어를 통해 회원 정보를 조회합니다.
        MemberVO member = memberService.getMemberById(memberId);

        if (member != null) {
            // 회원 정보가 있다면 DTO에 담아 반환
            MemberInfoDTO memberInfo = new MemberInfoDTO();
            memberInfo.setUsername(member.getNickname()); // 실제 사용자 이름으로 설정
            // 필요한 다른 필드도 설정
            return memberInfo;
        }

        // 회원 정보를 찾지 못한 경우
        log.warn("ID {}에 해당하는 회원을 찾을 수 없습니다.", memberId);
        return new MemberInfoDTO();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            // 수동으로 인증 처리
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 세션 생성
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            System.out.println("로그인 성공 - SessionId: " + session.getId());
            System.out.println("인증 정보: " + authentication.getPrincipal());

            return ResponseEntity.ok().body(Map.of("success", true, "sessionId", session.getId()));

        } catch (Exception e) {
            System.out.println("로그인 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HttpSession session = request.getSession(false);

        return ResponseEntity.ok(Map.of(
                "sessionId", session != null ? session.getId() : "null",
                "isAuthenticated", auth != null && auth.isAuthenticated(),
                "principal", auth != null ? auth.getPrincipal().toString() : "null",
                "name", auth != null ? auth.getName() : "null",
                "authorities", auth != null ? auth.getAuthorities().toString() : "null"
        ));
    }





}