package com.example.whosbookupdate.dto;


import lombok.Data;

import java.util.Date;

@Data
public class MemberResponseDto {

    private String memberId;
    private String email;
    private String nickname;
    private String password;
    private Date created_at;
    private Date updated_at;
    private String introduction;
    private String imageUrl;
}
