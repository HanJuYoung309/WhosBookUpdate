package com.example.whosbookupdate.dto;


import com.example.whosbookupdate.domain.BookVO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CurationResponseDto {

    private Long curationId;
    private Long memberId;
    private String categoryId;
    private String title;
    private String content;
    private String emoji;
    private String curationStatus;
    private String curationLikeCount;
    private Date createdAt;
    private Date updatedAt;
    private List<BookVO> books;

}
