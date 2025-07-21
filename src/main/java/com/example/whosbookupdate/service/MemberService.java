package com.example.whosbookupdate.service;

import com.example.whosbookupdate.domain.MemberVO;
import com.example.whosbookupdate.dto.MemberRegistrationDto;
import com.example.whosbookupdate.dto.MemberResponseDto;
import com.example.whosbookupdate.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

@Service
public class MemberService {


    @Autowired
    private  MemberMapper memberMapper;


    @Transactional
    public MemberVO join(MemberRegistrationDto registrationDto) {

       if(memberMapper.findByEmail(registrationDto.getEmail()) != null){
           //throw new DuplicateKeyException("이미 등록된 이메일입니다");
       }
       //비밀번호 암호화

        MemberVO member = new MemberVO();

        member.setEmail(registrationDto.getEmail());
        member.setNickname(registrationDto.getNickname());
        member.setIntroduction(registrationDto.getIntroduction());
        member.setPassword(registrationDto.getPassword());
        member.setMemberId(registrationDto.getMemberId());
        member.setImage(registrationDto.getImage());
        member.setStatus(registrationDto.getStatus());

        try{
            memberMapper.insertMember(member);
            return member;
        }catch (Exception e){
            System.err.println("회원가입 중 Mybatis 오류 발생: " + e.getMessage());
            throw new RuntimeException("회원가입 처리 중 알 수 없는 오류가 발생했습니다.", e);
        }

    }

    public MemberResponseDto login(String email, String password) {

        MemberVO member= memberMapper.findByEmail(email);

        //1.사용자 id존재여부 확인
        if(member == null){
            return null;
        }

        //2.비밀번호 일치여부 확인
        MemberResponseDto memberResponseDto = new MemberResponseDto();
        if(member.getPassword().equals(password)){
            return memberResponseDto;

        }
        return memberResponseDto;
    }


}
