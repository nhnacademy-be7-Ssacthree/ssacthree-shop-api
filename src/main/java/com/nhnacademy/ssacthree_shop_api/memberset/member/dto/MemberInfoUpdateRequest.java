package com.nhnacademy.ssacthree_shop_api.memberset.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class MemberInfoUpdateRequest {
    @NotBlank
    // TODO : 지금은 임시로 validation 하지않지만, 서비스시에는 해야함.
    private String loginId;

    @NotBlank
    @Size(max = 10)
    // 10글자만 주기로 함.
    private String customerName;

    @NotBlank
    @Size(max = 13)
    private String customerPhoneNumber;

    @NotBlank
    @Size(max = 50)
    private String customerEmail;

    @NotBlank
    @Size(max = 8)
    private String birth;
}
