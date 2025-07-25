package com.example.whosbookupdate.service;

import com.example.whosbookupdate.domain.CurationVO;
import com.example.whosbookupdate.dto.CurationResponseDto;
import com.example.whosbookupdate.mapper.CurationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

@Service
public class CurationService {

    private final CurationMapper curationMapper;

    @Autowired
    public CurationService(CurationMapper curationMapper) {
        this.curationMapper = curationMapper;
    }

    @Transactional
    public CurationVO createCuration(CurationResponseDto curationResponseDto) {

        CurationVO curationVO = new CurationVO();
        curationVO.setTitle(curationResponseDto.getTitle());
        curationVO.setMemberId(curationResponseDto.getMemberId());
        curationVO.setContent(curationResponseDto.getContent());
        curationVO.setEmoji(curationResponseDto.getEmoji());
        curationVO.setCurationStatus(curationResponseDto.getCurationStatus());
        curationVO.setCurationLikeCount(curationResponseDto.getCurationLikeCount());

        if(!isValidCurationStatus(curationResponseDto.getCurationStatus())) {
            throw new IllegalArgumentException("유효하지 않은 큐레이션 상태입니다.");
        }

        curationMapper.insertCuration(curationVO);

        return curationVO;

    }

    private boolean isValidCurationStatus(String status){

        return "PUBLIC".equals(status) || "PRIVATE".equals(status) || "DRAFT".equals(status);
    }

    public List<CurationVO> getCuration() {

        List<CurationVO> curationVOList= curationMapper.selectCuration();

        return curationVOList;
    }
}

