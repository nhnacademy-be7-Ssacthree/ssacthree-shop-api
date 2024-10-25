package com.nhnacademy.ssacthree_shop_api.memberset.member.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
//일단은 비밀번호 안줌..
public class MemberGetResponse {

    private String memberLoginId;
    private String memberName;

    private String memberBirthdate;
    private LocalDateTime memberCreatedAt;
    private String memberStatus;
    private long memberPoint;

}
