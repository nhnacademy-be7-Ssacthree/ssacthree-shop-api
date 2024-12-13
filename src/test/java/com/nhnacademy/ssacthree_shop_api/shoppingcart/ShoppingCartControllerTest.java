package com.nhnacademy.ssacthree_shop_api.shoppingcart;


import com.nhnacademy.ssacthree_shop_api.shoppingcart.controller.ShoppingCartController;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.dto.ShoppingCartItemResponse;
import com.nhnacademy.ssacthree_shop_api.shoppingcart.service.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = ShoppingCartController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartService shoppingCartService;

    private List<ShoppingCartItemResponse> cartResponseList;
    private ShoppingCartItemResponse cartItemResponse;

    @BeforeEach
    void setUp() {
        cartItemResponse = new ShoppingCartItemResponse(
            1L,
            "Book Name",
            2,
            3000,
            "http://example.com/thumbnail.jpg"
        );
        cartResponseList = List.of(cartItemResponse);
    }

    @Test
    void testGetAllCartItems() throws Exception {
        when(shoppingCartService.getShoppingCartItemsBycustomerId("test-user"))
            .thenReturn(cartResponseList);

        mockMvc.perform(get("/api/shop/carts")
                .header("X-USER-ID", "test-user"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].title").value("Book Name"))
            .andExpect(jsonPath("$[0].quantity").value(2))
            .andExpect(jsonPath("$[0].price").value(3000))
            .andExpect(jsonPath("$[0].bookThumbnailImageUrl").value("http://example.com/thumbnail.jpg"));

        verify(shoppingCartService, times(1)).getShoppingCartItemsBycustomerId("test-user");
    }

    @Test
    void testGetRandomBook() throws Exception {
        when(shoppingCartService.getBookByBookId(1L))
            .thenReturn(cartItemResponse);

        mockMvc.perform(get("/api/shop/carts/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Book Name"))
            .andExpect(jsonPath("$.quantity").value(2))
            .andExpect(jsonPath("$.price").value(3000))
            .andExpect(jsonPath("$.bookThumbnailImageUrl").value("http://example.com/thumbnail.jpg"));

        verify(shoppingCartService, times(1)).getBookByBookId(1L);
    }

    @Test
    void testSaveCart() throws Exception {
        doNothing().when(shoppingCartService).saveCart(anyList(), eq(123L));

        mockMvc.perform(post("/api/shop/carts/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{\"bookId\":1,\"quantity\":2}]")
                .param("customerId", "123"))
            .andExpect(status().isOk());

        verify(shoppingCartService, times(1)).saveCart(anyList(), eq(123L));
    }

    @Test
    void testGetBook() throws Exception {
        when(shoppingCartService.getBookByBookId(1L))
            .thenReturn(cartItemResponse);

        mockMvc.perform(get("/api/shop/carts/add")
                .param("bookId", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Book Name"))
            .andExpect(jsonPath("$.quantity").value(2))
            .andExpect(jsonPath("$.price").value(3000))
            .andExpect(jsonPath("$.bookThumbnailImageUrl").value("http://example.com/thumbnail.jpg"));

        verify(shoppingCartService, times(1)).getBookByBookId(1L);
    }

    @Test
    void testSaveCartonLogout() throws Exception {
        doNothing().when(shoppingCartService).saveCartonLogout(eq("test-user"), anyList());

        mockMvc.perform(post("/api/shop/carts/logout")
                .header("X-USER-ID", "test-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{\"bookId\":1,\"quantity\":2}]"))
            .andExpect(status().isOk());

        verify(shoppingCartService, times(1)).saveCartonLogout(eq("test-user"), anyList());
    }
}
