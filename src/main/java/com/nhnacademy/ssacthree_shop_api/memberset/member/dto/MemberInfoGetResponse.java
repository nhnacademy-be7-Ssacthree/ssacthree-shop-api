package com.nhnacademy.ssacthree_shop_api.memberset.member.dto;

import static com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.QMemberGrade.memberGrade;

import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
//일단은 비밀번호 안줌..
public class MemberInfoGetResponse {

    public MemberInfoGetResponse(Member member) {

        this.memberLoginId = member.getMemberLoginId();
        this.customerName = member.getCustomer().getCustomerName();
        this.customerPhoneNumber = member.getCustomer().getCustomerPhoneNumber();
        this.customerEmail = member.getCustomer().getCustomerEmail();
        this.memberBirthdate = member.getMemberBirthdate();
        this.memberPoint = member.getMemberPoint();

    }


    private String memberLoginId;
    private String customerName;
    private String customerPhoneNumber;
    private String customerEmail;
    private String memberBirthdate;
    private int memberPoint;

}
