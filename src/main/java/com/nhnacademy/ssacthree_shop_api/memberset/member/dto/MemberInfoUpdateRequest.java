package com.nhnacademy.ssacthree_shop_api.memberset.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class MemberInfoUpdateRequest {


    private String customerName;


    private String customerPhoneNumber;


    private String customerEmail;

}
