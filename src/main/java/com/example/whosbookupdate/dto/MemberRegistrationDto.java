package com.example.whosbookupdate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRegistrationDto {

    private int memberId;
    private String email;
    private String nickname;
    private String password;
    private String introduction;
    private String image;
    private String status;


}
