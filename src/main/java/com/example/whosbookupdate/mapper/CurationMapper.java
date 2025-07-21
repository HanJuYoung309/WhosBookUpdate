package com.example.whosbookupdate.mapper;


import com.example.whosbookupdate.domain.CurationVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CurationMapper {
    void insertCuration(CurationVO curationVO);
}
