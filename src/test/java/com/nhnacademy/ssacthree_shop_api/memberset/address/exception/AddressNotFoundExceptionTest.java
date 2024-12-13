package com.nhnacademy.ssacthree_shop_api.memberset.address.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressNotFoundExceptionTest {

    @Test
    void testAddressNotFoundExceptionMessage(){
        String errorMessage = "Address not found";

        AddressNotFoundException exception = new AddressNotFoundException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}
