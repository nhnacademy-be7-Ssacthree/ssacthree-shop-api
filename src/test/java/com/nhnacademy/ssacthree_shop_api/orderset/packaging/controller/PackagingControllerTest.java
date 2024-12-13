package com.nhnacademy.ssacthree_shop_api.orderset.packaging.controller;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto.PackagingCreateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto.PackagingGetResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto.PackagingUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.service.PackagingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PackagingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PackagingService packagingService;

    @InjectMocks
    private PackagingController packagingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(packagingController).build();
    }

    @Test
    void testGetAllPackaging() throws Exception {
        // Given
        PackagingGetResponse packagingGetResponse = new PackagingGetResponse(1L, "포장지1", 1000, "image1.jpg");
        when(packagingService.getAllPackaging()).thenReturn(List.of(packagingGetResponse));

        // When & Then
        mockMvc.perform(get("/api/shop/admin/packaging"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].packagingName").value("포장지1"))
                .andExpect(jsonPath("$[0].packagingPrice").value(1000))
                .andExpect(jsonPath("$[0].packagingImageUrl").value("image1.jpg"));

        verify(packagingService, times(1)).getAllPackaging();
    }

    @Test
    void testCreatePackaging() throws Exception {
        // Given
        PackagingCreateRequest packagingCreateRequest = new PackagingCreateRequest("포장지2", 2000, "image2.jpg");
        MessageResponse messageResponse = new MessageResponse("생성 성공");
        when(packagingService.savePackaging(any(PackagingCreateRequest.class))).thenReturn(messageResponse);

        // When & Then
        mockMvc.perform(post("/api/shop/admin/packaging")
                        .contentType("application/json")
                        .content("{\"name\":\"포장지2\", \"price\":2000, \"imageUrl\":\"image2.jpg\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("생성 성공"));

        verify(packagingService, times(1)).savePackaging(any(PackagingCreateRequest.class));
    }

    @Test
    void testUpdatePackaging() throws Exception {
        // Given
        PackagingUpdateRequest packagingUpdateRequest = new PackagingUpdateRequest("포장지3", 3000, "image3.jpg");
        MessageResponse messageResponse = new MessageResponse("수정 성공");
        when(packagingService.updatePackaging(anyString(), any(PackagingUpdateRequest.class))).thenReturn(messageResponse);

        // When & Then
        mockMvc.perform(put("/api/shop/admin/packaging/{packaging-id}", "1")
                        .contentType("application/json")
                        .content("{\"packagingName\":\"포장지3\", \"packagingPrice\":3000, \"packagingImageUrl\":\"image3.jpg\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("수정 성공"));

        verify(packagingService, times(1)).updatePackaging(eq("1"), any(PackagingUpdateRequest.class));
    }

    @Test
    void testDeletePackaging() throws Exception {
        // Given
        MessageResponse messageResponse = new MessageResponse("삭제 성공");
        when(packagingService.deletePackaging(anyString())).thenReturn(messageResponse);

        // When & Then
        mockMvc.perform(delete("/api/shop/admin/packaging/{packaging-id}", "1"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message").value("삭제 성공"));

        verify(packagingService, times(1)).deletePackaging(eq("1"));
    }
}
