package com.nhnacademy.ssacthree_shop_api.memberset.member.exception;

import com.nhnacademy.ssacthree_shop_api.commons.exception.ConflictException;

public class AlreadyMemberException extends ConflictException {

    public AlreadyMemberException(String message) {
        super(message);
    }
}
