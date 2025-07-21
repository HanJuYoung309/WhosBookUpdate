package com.example.whosbookupdate.dto;

import lombok.Data;

@Data
public class CurationRequestDto {

    private String title;
    private int memberId;
    private String categoryId;
    private String content;
    private String emoji;
    private String curationStatus;
    private int curationLikeCount;



}
