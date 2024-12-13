package com.nhnacademy.ssacthree_shop_api.memberset.member.event;

import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberRegisteredEvent {
    private final Member member;
}
