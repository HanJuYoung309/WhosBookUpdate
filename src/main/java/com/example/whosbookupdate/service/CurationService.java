package com.example.whosbookupdate.service;

import com.example.whosbookupdate.domain.CurationVO;
import com.example.whosbookupdate.dto.CurationRequestDto;
import com.example.whosbookupdate.dto.CurationResponseDto;
import com.example.whosbookupdate.mapper.CurationMapper;
import com.example.whosbookupdate.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CurationService {

    private final CurationMapper curationMapper;

    @Autowired
    public CurationService(CurationMapper curationMapper) {
        this.curationMapper = curationMapper;
    }


    private boolean isValidCurationStatus(String status){

        return "PUBLIC".equals(status) || "PRIVATE".equals(status) || "DRAFT".equals(status);
    }

    public List<CurationVO> getCuration() {

        List<CurationVO> curationVOList= curationMapper.selectCuration();

        return curationVOList;
    }


    @Transactional
    public CurationVO createCuration(CurationResponseDto curationResponseDto, Long memberId) {

        // 1. DTO 객체를 VO(Value Object)로 변환
        CurationVO curationVO = new CurationVO();
        curationVO.setMemberId(memberId);
        curationVO.setTitle(curationResponseDto.getTitle());
        curationVO.setContent(curationResponseDto.getContent());
        curationVO.setCategoryId(curationResponseDto.getCategoryId());
        curationVO.setEmoji(curationResponseDto.getEmoji());
        curationVO.setCurationStatus("PUBLIC");
        curationVO.setCurationLikeCount(0);

        // 3. Mapper를 사용하여 데이터베이스에 CurationVO 삽입
        curationMapper.insertCuration(curationVO);

        // 4. 삽입된 객체 반환 (DB에서 자동 생성된 ID 등을 포함할 수 있음)
        return curationVO;
    }
}

