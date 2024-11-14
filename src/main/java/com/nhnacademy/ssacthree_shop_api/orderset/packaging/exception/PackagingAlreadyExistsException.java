package com.nhnacademy.ssacthree_shop_api.orderset.packaging.exception;

public class PackagingAlreadyExistsException extends RuntimeException{
    public PackagingAlreadyExistsException(String message) {
        super(message);
    }
}
