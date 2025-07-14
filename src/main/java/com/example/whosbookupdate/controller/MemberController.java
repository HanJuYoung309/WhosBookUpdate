package com.example.whosbookupdate.controller;

import com.example.whosbookupdate.domain.MemberVO;
import com.example.whosbookupdate.dto.MemberRegistrationDto;
import com.example.whosbookupdate.dto.MemberResponseDto;
import com.example.whosbookupdate.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; // @Controller 사용
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;


@Controller
@RequestMapping("/member")
@CrossOrigin(origins = "http://localhost:8082")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
    @ResponseBody // 이 메서드의 반환 값을 HTTP 응답 본문으로 직접 보냅니다.
    public MemberResponseDto join(@RequestBody MemberRegistrationDto registrationDto) {
        // 1. 입력 유효성 검사
        if (registrationDto.getMemberId() == null || registrationDto.getPassword() == null) {
            // 필수 입력값이 누락되면 400 Bad Request 에러 발생
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자 ID와 비밀번호는 필수 입력 값입니다.");
        }

        try {
            // 2. 회원가입 비즈니스 로직 수행
            MemberVO registeredMember = memberService.join(registrationDto);

            // 3. 응답 DTO로 변환하여 반환 (비밀번호 등 민감 정보는 제외)
            MemberResponseDto responseDto = new MemberResponseDto();

           responseDto.setMemberId(registeredMember.getMemberId());
           responseDto.setPassword(registeredMember.getPassword());
           responseDto.setEmail(registeredMember.getEmail());
           responseDto.setNickname(registeredMember.getNickname());
           responseDto.setIntroduction(registeredMember.getIntroduction());
           responseDto.setImageUrl(registrationDto.getImage());

           //System.out.println(responseDto);

            return responseDto;

        } catch (DuplicateKeyException e) {

            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 사용자 ID 또는 이메일입니다.", e);
        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "회원가입 중 오류가 발생했습니다.", e);

        }
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<MemberResponseDto> login(@RequestBody  MemberRegistrationDto registrationDto, HttpSession session) {

        if(registrationDto.getMemberId() == null || registrationDto.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"아이디와 비밀번호는 필수 입력 값입니다.");
        }

        MemberResponseDto loginResponseDto= memberService.login(registrationDto.getMemberId(),registrationDto.getPassword());

        if(loginResponseDto == null) {
            session.setAttribute("loginResponseDto",loginResponseDto);
            return ResponseEntity.ok(loginResponseDto);
        }else {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED,"아이디 또는 비밀번호가 일치하지 않습니다");
        }

    }
}