package com.nhnacademy.ssacthree_shop_api.memberset.membergrade.exception;

import com.nhnacademy.ssacthree_shop_api.commons.exception.NotFoundException;

public class MemberGradeNotFoundException extends NotFoundException {

    public MemberGradeNotFoundException(String message) {
        super(message);
    }
}
