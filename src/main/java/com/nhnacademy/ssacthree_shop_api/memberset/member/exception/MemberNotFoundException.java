package com.nhnacademy.ssacthree_shop_api.memberset.member.exception;

import com.nhnacademy.ssacthree_shop_api.commons.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException(String message) {
        super(message);
    }
}
