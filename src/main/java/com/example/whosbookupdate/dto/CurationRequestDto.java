package com.example.whosbookupdate.dto;

import com.example.whosbookupdate.domain.BookVO;
import lombok.Data;

import java.util.List;

@Data
public class CurationRequestDto {

    private String title;
    private Long memberId;
    private String categoryId;
    private String content;
    private String emoji;
    private String curationStatus;
    private int curationLikeCount;
    private List<BookVO> books;


}
