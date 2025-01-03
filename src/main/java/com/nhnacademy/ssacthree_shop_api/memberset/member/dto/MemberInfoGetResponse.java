package com.nhnacademy.ssacthree_shop_api.memberset.member.dto;


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
        this.customerId = member.getCustomer().getCustomerId();
        this.memberLoginId = member.getMemberLoginId();
        this.customerName = member.getCustomer().getCustomerName();
        this.customerPhoneNumber = member.getCustomer().getCustomerPhoneNumber();
        this.customerEmail = member.getCustomer().getCustomerEmail();
        this.memberBirthdate = member.getMemberBirthdate();
        this.memberPoint = member.getMemberPoint();
        this.memberGradeName = member.getMemberGrade().getMemberGradeName();
        this.memberGradePointSave = member.getMemberGrade().getMemberGradePointSave();
    }

    private Long customerId;
    private String memberLoginId;
    private String customerName;
    private String customerPhoneNumber;
    private String customerEmail;
    private String memberBirthdate;
    private int memberPoint;
    private String memberGradeName;
    private float memberGradePointSave;

}
