package com.example.whosbookupdate.controller;

import com.example.whosbookupdate.domain.MemberVO;
import com.example.whosbookupdate.dto.MemberRegistrationDto;
import com.example.whosbookupdate.dto.MemberResponseDto;
import com.example.whosbookupdate.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller; // @Controller 사용
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.util.Map;


@Controller
@RequestMapping("/member")
@CrossOrigin(origins = "http://localhost:8081")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
    @ResponseBody
    public MemberResponseDto join(@RequestBody MemberRegistrationDto registrationDto) {
        // 1. 입력 유효성 검사 (컨트롤러에서 기본적인 유효성 검사는 유지)
        if (registrationDto.getEmail() == null || registrationDto.getPassword() == null ||
                registrationDto.getNickname() == null) { // 닉네임도 필수라고 가정
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "필수 입력값이 누락되었습니다 (이메일, 비밀번호, 닉네임 등).");
        }

        try {
            // 2. 회원가입 비즈니스 로직 수행
            MemberVO registeredMember = memberService.join(registrationDto);

            MemberResponseDto responseDto = new MemberResponseDto();
            responseDto.setMemberId(registeredMember.getMemberId());
            responseDto.setEmail(registeredMember.getEmail());
            responseDto.setNickname(registeredMember.getNickname());
            responseDto.setIntroduction(registeredMember.getIntroduction());
            responseDto.setImageUrl(registeredMember.getImageUrl());

            return responseDto;

        } catch (DuplicateKeyException e) {
            // 이메일 또는 ID 중복 시
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 사용자 이메일입니다.", e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입 중 예상치 못한 오류가 발생했습니다.", e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> login(@RequestBody  MemberRegistrationDto registrationDto, HttpSession session) {

        if(registrationDto.getEmail() == null || registrationDto.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"아이디와 비밀번호는 필수 입력 값입니다.");
        }

        MemberResponseDto loginResponseDto= memberService.login(registrationDto.getEmail(),registrationDto.getPassword());

        if(loginResponseDto == null) {
            session.setAttribute("loginResponseDto",loginResponseDto);
            return ResponseEntity.ok(loginResponseDto);
        }else {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED,"아이디 또는 비밀번호가 일치하지 않습니다");
        }

    }


    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate(); // 세션 무효화

        return ResponseEntity.ok("로그아웃 성공");
    }
}