package com.nhnacademy.ssacthree_shop_api.bookset.book.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.BookDto;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(controllers = BookRegisterController.class,
        excludeAutoConfiguration = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
class BookRegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    private ObjectMapper objectMapper;
    private BookDto validBookDto;
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        validBookDto = new BookDto("Sample Book", "Index", "Description", "1234567890123",
                LocalDateTime.now(), 20000, 15000, true, 10, "http://example.com/img.jpg", 0, 10);
    }
    @Test
    void registerBook_withValidData_shouldReturnCreated() throws Exception {
        Mockito.doNothing().when(bookService).registerBook(any(BookDto.class));
        mockMvc.perform(post("/api/books/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookDto)))
                .andExpect(status().isCreated());
    }
}


