package com.example.whosbookupdate.controller;


import com.example.whosbookupdate.domain.MemberVO;
import com.example.whosbookupdate.dto.LoginRequest;
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
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;


@Log4j2
@Controller
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

    @GetMapping("/")
    public String main() {
        System.out.println("---메인-----");
        return "index";

    }

    // 로그인 페이지 보기
    @GetMapping("/login")
    public String loginGET(String error, String logout) {
        log.info("login 페이지");
        //log.info("logout.............................." + logout);

        return "member/login";

    }

    // 회원가입 페이지 보기
    @GetMapping("/join")
    public String joinGET(String error, String logout) {


        log.info("회원가입 페이지");
        log.info("logout.............................." + logout);

        return "member/join";

//    }

    }

    //회원가입 처리
    @PostMapping("/register")
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

    //로그인 처리
    /**
     * 로그인 API (POST 요청)
     * 인증 성공 후 SecurityContext를 HttpSession에 명시적으로 저장합니다.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 중요: SecurityContext를 HttpSession에 명시적으로 저장
            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

            log.info("로그인 성공! 인증 객체: {}", authentication); // 로그인 성공 로그 추가
            log.info("인증된 사용자 이름: {}", authentication.getName());
            log.info("인증된 사용자 권한: {}", authentication.getAuthorities());

            return ResponseEntity.ok("로그인 성공");
        } catch (Exception e) {
            log.warn("로그인 실패: {}", e.getMessage()); // 로그인 실패 로그
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 아이디 또는 패스워드입니다");
        }
    }







}