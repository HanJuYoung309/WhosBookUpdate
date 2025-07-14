package com.example.whosbookupdate.domain;

import lombok.Data;

import java.util.Date;

@Data
public class MemberVO {

    private String memberId;
    private String email;
    private String status;
    private String nickname;
    private String password;
    private Date created_at;
    private Date updated_at;
    private String introduction;
    private String image;
    private String imageUrl;

}
