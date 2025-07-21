package com.example.whosbookupdate.domain;


import lombok.Data;

import java.util.Date;

@Data
public class CurationVO {

    private Long curationId;
    private String memberId;
    private String categoryId;
    private String title;
    private String content;
    private String emoji;
    private String curationStatus;
    private String curationLikeCount;
    private Date createdAt;
    private Date updatedAt;
}
