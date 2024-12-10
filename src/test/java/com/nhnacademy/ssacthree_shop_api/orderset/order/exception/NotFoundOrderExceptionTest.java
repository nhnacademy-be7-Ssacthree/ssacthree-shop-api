package com.nhnacademy.ssacthree_shop_api.orderset.order.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotFoundOrderExceptionTest {

    @Test
    void NotFoundOrderExceptionMessage(){

        String expectedMsg = "Order not found";

        NotFoundOrderException notFoundOrderException =
                assertThrows(NotFoundOrderException.class, () -> {
                    throw new NotFoundOrderException(expectedMsg);
                });


        assertEquals(expectedMsg, notFoundOrderException.getMessage());
    }
}
