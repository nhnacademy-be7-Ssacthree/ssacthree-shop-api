package com.nhnacademy.ssacthree_shop_api.memberset.address.dto;

import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AddressCreateRequest {

    private Member member; // 이거 있어야하나? 어케 가져오지

    private String addressAlias;
    private String addressDetail;
    private String addressPostalNumber;

}
