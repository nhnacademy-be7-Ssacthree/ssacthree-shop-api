package com.nhnacademy.ssacthree_shop_api.memberset.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberRegisterRequest {
    private String loginId;
    @Setter
    private String loginPassword;
    private String customerName;
    private String customerPhoneNumber;
    private String customerEmail;
    private String birth;
}
