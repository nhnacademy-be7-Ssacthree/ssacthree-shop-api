package com.nhnacademy.ssacthree_shop_api.memberset.pointsaverule.exception;

import com.nhnacademy.ssacthree_shop_api.commons.exception.NotFoundException;

public class PointSaveRuleNotFoundException extends NotFoundException {

    public PointSaveRuleNotFoundException(String message) {
        super(message);
    }
}
