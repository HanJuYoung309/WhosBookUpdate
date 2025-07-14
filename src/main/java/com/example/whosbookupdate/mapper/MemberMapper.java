package com.example.whosbookupdate.mapper;

import com.example.whosbookupdate.domain.MemberVO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MemberMapper {
    int insertMember(MemberVO member);

    MemberVO findByMemberId(String memberId);
}
