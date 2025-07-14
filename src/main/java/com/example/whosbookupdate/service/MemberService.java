package com.example.whosbookupdate.service;

import com.example.whosbookupdate.domain.MemberVO;
import com.example.whosbookupdate.dto.MemberRegistrationDto;
import com.example.whosbookupdate.dto.MemberResponseDto;
import com.example.whosbookupdate.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

@Service
public class MemberService {


    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberVO join(MemberRegistrationDto registrationDto) {

//     if(memberMapper.findByMemberId(registrationDto.getMemberId())!=null){
//         throw new DuplicateKeyException("이미 존재하는 사용자 아이디입니다 ");
//     }

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

    public MemberResponseDto login(String memberId, String password) {

        MemberVO member= memberMapper.findByMemberId(memberId);

        //1.사용자 id존재여부 확인
        if(member == null){
            return null;
        }

        //2.비밀번호 일치여부 확인
        if(passwordEncoder.matches(password, member.getPassword())){

            MemberResponseDto memberResponseDto = new MemberResponseDto();
            memberResponseDto.setMemberId(member.getMemberId());
            memberResponseDto.setNickname(member.getNickname());
            memberResponseDto.setIntroduction(member.getIntroduction());
            memberResponseDto.setEmail(member.getEmail());
            memberResponseDto.setImageUrl(member.getImage());

            return memberResponseDto;
        }else{
            return null;
        }


    }

    public MemberRegistrationDto findbyIdandPassword(MemberResponseDto memberResponseDto) {

        MemberRegistrationDto memberRegistrationDto = new MemberRegistrationDto();
        memberRegistrationDto.setMemberId(memberResponseDto.getMemberId());
        memberRegistrationDto.setPassword(memberResponseDto.getPassword());

        return memberRegistrationDto;
    }
}
