package com.nhnacademy.ssacthree_shop_api.orderset.packaging.service;

import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.domain.Packaging;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto.PackagingCreateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto.PackagingGetResponse;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.dto.PackagingUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.exception.PackagingAlreadyExistsException;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.exception.PackagingNotFoundException;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.repository.PackagingRepository;
import com.nhnacademy.ssacthree_shop_api.orderset.packaging.service.impl.PackagingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PackagingServiceImplTest {

    @Mock
    private PackagingRepository packagingRepository;

    @InjectMocks
    private PackagingServiceImpl packagingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPackaging() {
        // Given
        Packaging packaging = new Packaging("포장지1", 1000, "image1.jpg");
        when(packagingRepository.findAll()).thenReturn(List.of(packaging));

        // When
        List<PackagingGetResponse> response = packagingService.getAllPackaging();

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("포장지1", response.get(0).getPackagingName());
    }

    @Test
    void testSavePackaging_Success() {
        // Given
        PackagingCreateRequest request = new PackagingCreateRequest("포장지2", 2000, "image2.jpg");
        when(packagingRepository.existsByPackagingName(request.getName())).thenReturn(false);

        // When
        MessageResponse response = packagingService.savePackaging(request);

        // Then
        assertEquals("생성 성공", response.getMessage());
        verify(packagingRepository, times(1)).save(any(Packaging.class));
    }

    @Test
    void testSavePackaging_AlreadyExists() {
        // Given
        PackagingCreateRequest request = new PackagingCreateRequest("포장지1", 1000, "image1.jpg");
        when(packagingRepository.existsByPackagingName(request.getName())).thenReturn(true);

        // When & Then
        PackagingAlreadyExistsException exception = assertThrows(PackagingAlreadyExistsException.class, () -> {
            packagingService.savePackaging(request);
        });
        assertEquals("포장지 이름이 이미 존재합니다.", exception.getMessage());
    }

    @Test
    void testUpdatePackaging_Success() {
        // Given
        PackagingUpdateRequest request = new PackagingUpdateRequest("포장지3", 3000, "image3.jpg");
        Packaging existingPackaging = new Packaging("포장지1", 1000, "image1.jpg");
        when(packagingRepository.findById(1L)).thenReturn(Optional.of(existingPackaging));

        // When
        MessageResponse response = packagingService.updatePackaging("1", request);

        // Then
        assertEquals("수정 성공", response.getMessage());
        assertEquals("포장지3", existingPackaging.getPackagingName());
        assertEquals(3000, existingPackaging.getPackagingPrice());
        verify(packagingRepository, times(1)).save(existingPackaging);
    }

    @Test
    void testUpdatePackaging_NotFound() {
        // Given
        PackagingUpdateRequest request = new PackagingUpdateRequest("포장지3", 3000, "image3.jpg");
        when(packagingRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        PackagingNotFoundException exception = assertThrows(PackagingNotFoundException.class, () -> {
            packagingService.updatePackaging("1", request);
        });
        assertEquals("해당 포장지가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    void testDeletePackaging_Success() {
        // Given
        Packaging packaging = new Packaging("포장지1", 1000, "image1.jpg");
        when(packagingRepository.findById(1L)).thenReturn(Optional.of(packaging));

        // When
        MessageResponse response = packagingService.deletePackaging("1");

        // Then
        assertEquals("삭제 성공", response.getMessage());
        verify(packagingRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeletePackaging_NotFound() {
        // Given
        when(packagingRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        PackagingNotFoundException exception = assertThrows(PackagingNotFoundException.class, () -> {
            packagingService.deletePackaging("1");
        });
        assertEquals("해당 포장지가 존재하지 않습니다.", exception.getMessage());
    }
}
