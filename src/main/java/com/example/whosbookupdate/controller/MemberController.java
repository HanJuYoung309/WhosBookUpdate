package com.example.whosbookupdate.controller;


import com.example.whosbookupdate.domain.MemberVO;
import com.example.whosbookupdate.dto.LoginRequest;
import com.example.whosbookupdate.dto.MemberRegistrationDto;
import com.example.whosbookupdate.dto.MemberResponseDto;
import com.example.whosbookupdate.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;


@Log4j2
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    private final AuthenticationManager authenticationManager;

    public MemberController(MemberService memberService, AuthenticationManager authenticationManager) {
        this.memberService = memberService;
        this.authenticationManager = authenticationManager;
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        try{
            Authentication authentication= authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok("로그인 성공");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("유효하지않은 아이디 또는 패스워드입니다");
        }

    }




}