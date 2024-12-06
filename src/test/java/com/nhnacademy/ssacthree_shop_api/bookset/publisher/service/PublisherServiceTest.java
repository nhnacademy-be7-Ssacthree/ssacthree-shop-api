package com.nhnacademy.ssacthree_shop_api.bookset.publisher.service;

import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherCreateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherGetResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherService publisherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePublisher() {
        // Arrange
        PublisherCreateRequest request = new PublisherCreateRequest();
        request.setPublisherName("New Publisher");

        Publisher savedPublisher = new Publisher("New Publisher");
        when(publisherRepository.save(any(Publisher.class))).thenReturn(savedPublisher);

        // Act
        Publisher result = publisherService.createPublisher(request);

        // Assert
        assertEquals("New Publisher", result.getPublisherName());
        verify(publisherRepository, times(1)).save(any(Publisher.class));
    }

    @Test
    void testGetAllPublishers() {
        // Arrange
        Pageable pageable = Pageable.unpaged();
        Page<PublisherGetResponse> expectedPage = mock(Page.class);
        when(publisherRepository.findAllPublisher(pageable)).thenReturn(expectedPage);

        // Act
        Page<PublisherGetResponse> result = publisherService.getAllPublishers(pageable);

        // Assert
        assertEquals(expectedPage, result);
        verify(publisherRepository, times(1)).findAllPublisher(pageable);
    }

    @Test
    void testUpdatePublisher() {
        // Arrange
        PublisherUpdateRequest request = new PublisherUpdateRequest();
        request.setPublisherId(1L);

        Publisher existingPublisher = new Publisher(1L, "Existing Publisher", true);
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(existingPublisher));
        when(publisherRepository.save(any(Publisher.class))).thenReturn(existingPublisher);

        // Act
        Publisher result = publisherService.updatePublisher(request);

        // Assert
        assertNotNull(result);
        assertFalse(result.isPublisherIsUsed()); // 상태가 반전되었는지 확인
        verify(publisherRepository, times(1)).findById(1L);
        verify(publisherRepository, times(1)).save(any(Publisher.class));
    }

    @Test
    void testSavePublisherFromCsv() {
        // This test requires mocking the CSV reading logic.
        // You can implement it based on how you choose to test CSV file reading.
    }

    @Test
    void testGetAllPublisherList() {
        // Arrange
        List<PublisherGetResponse> expectedList = Collections.singletonList(new PublisherGetResponse(1L, "Publisher 1", true));
        when(publisherRepository.findAllPublisherList()).thenReturn(expectedList);

        // Act
        List<PublisherGetResponse> result = publisherService.getAllPublisherList();

        // Assert
        assertEquals(expectedList, result);
        verify(publisherRepository, times(1)).findAllPublisherList();
    }

    @Test
    void testUpdatePublisher_InvalidId() {
        // Arrange
        PublisherUpdateRequest request = new PublisherUpdateRequest();
        request.setPublisherId(0L); // 잘못된 ID 값 (0 이하)

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> publisherService.updatePublisher(request)
        );

        assertEquals("출판사 ID가 잘못되었습니다.", exception.getMessage());
        verify(publisherRepository, never()).findById(anyLong());
        verify(publisherRepository, never()).save(any(Publisher.class));
    }
}
