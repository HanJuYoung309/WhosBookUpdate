package com.example.whosbookupdate.mapper;


import com.example.whosbookupdate.domain.CurationVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CurationMapper {
    int insertCuration(CurationVO curationVO);

    List<CurationVO> selectCuration();
}
