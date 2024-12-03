package com.nhnacademy.ssacthree_shop_api.memberset.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookCommonService;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.request.BookLikeRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.booklike.dto.response.BookLikeResponse;
import com.nhnacademy.ssacthree_shop_api.commons.dto.MessageResponse;
import com.nhnacademy.ssacthree_shop_api.commons.paging.PageRequestBuilder;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberRegisterRequest;
import com.nhnacademy.ssacthree_shop_api.memberset.member.service.MemberService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
class MemberControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MemberService memberService;

    @Mock
    private BookCommonService bookCommonService;

    @InjectMocks
    private MemberController memberController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    void testRegisterMember() throws Exception {
        MemberRegisterRequest request = new MemberRegisterRequest(
            "testUser",
            "password123",
            "Test User",
            "010-1234-5678",
            "testuser@example.com",
            "1990-01-01"
        );

        MessageResponse messageResponse = new MessageResponse("생성 성공");

        when(memberService.registerMember(any(MemberRegisterRequest.class))).thenReturn(
            messageResponse);

        mockMvc.perform(post("/api/shop/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("생성 성공"));
    }

    @Test
    void testDeleteMember() throws Exception {
        String memberId = "testUser";

        MessageResponse messageResponse = new MessageResponse("삭제 성공");

        when(memberService.deleteMember(memberId)).thenReturn(messageResponse);

        mockMvc.perform(delete("/api/shop/members")
                .header("X-USER-ID", memberId))
            .andExpect(status().isOk());
    }

    @Test
    void testGetMemberInfo() throws Exception {
        String memberId = "testUser";

        MemberInfoGetResponse response = new MemberInfoGetResponse(
            1L,
            memberId,
            "Test User",
            "010-1234-5678",
            "testuser@example.com",
            "1990-01-01",
            1000,
            "Gold",
            1.5f
        );

        when(memberService.getMemberInfoById(memberId)).thenReturn(response);

        mockMvc.perform(get("/api/shop/members/my-page")
                .header("X-USER-ID", memberId))
            .andExpect(status().isOk());
    }

    @Test
    void testUpdateMemberInfo() throws Exception {
        String memberId = "testUser";

        MemberInfoUpdateRequest request = new MemberInfoUpdateRequest(
            "Updated User",
            "010-9876-5432",
            "updateduser@example.com"
        );

        MessageResponse messageResponse = new MessageResponse("수정 성공");

        when(memberService.updateMember(eq(memberId),
            any(MemberInfoUpdateRequest.class))).thenReturn(messageResponse);

        mockMvc.perform(put("/api/shop/members/my-page")
                .header("X-USER-ID", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    void testGetCustomerId() throws Exception {
        String memberId = "testUser";
        Long customerId = 1L;

        when(memberService.getCustomerIdByMemberLoginId(memberId)).thenReturn(customerId);

        mockMvc.perform(get("/api/shop/members/id")
                .header("X-USER-ID", memberId))
            .andExpect(status().isOk());
    }

    @Test
    void testGetBooksByMemberId() throws Exception {
        String memberId = "testUser";
        Long customerId = 1L;

        int page = 0;
        int size = 10;
        String[] sort = new String[]{"bookName:asc"};

        Pageable pageable = PageRequestBuilder.createPageable(page, size, sort);

        when(memberService.getCustomerIdByMemberLoginId(memberId)).thenReturn(customerId);

        Page<BookListResponse> books = new PageImpl<>(List.of(), pageable, 0);

        when(bookCommonService.getBooksByMemberId(any(Pageable.class), eq(customerId))).thenReturn(
            books);

        mockMvc.perform(get("/api/shop/members/my-page/likes")
                .header("X-USER-ID", memberId)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("sort", sort))
            .andExpect(status().isOk());
    }

    @Test
    void testGetLikedBooksIdForCurrentUser() throws Exception {
        String memberId = "testUser";
        Long customerId = 1L;

        when(memberService.getCustomerIdByMemberLoginId(memberId)).thenReturn(customerId);

        List<Long> bookIdList = List.of(101L, 102L, 103L);

        when(bookCommonService.getLikedBooksIdForCurrentUser(customerId)).thenReturn(bookIdList);

        mockMvc.perform(get("/api/shop/members/likeList")
                .header("X-USER-ID", memberId))
            .andExpect(status().isOk());
    }

    @Test
    void testCreateBookLikeByMemberId() throws Exception {
        String memberId = "testUser";
        Long customerId = 1L;

        BookLikeRequest request = new BookLikeRequest();

        BookLikeResponse response = new BookLikeResponse(request, 10L);

        when(memberService.getCustomerIdByMemberLoginId(memberId)).thenReturn(customerId);
        when(bookCommonService.saveBookLike(any(BookLikeRequest.class), eq(customerId))).thenReturn(
            response);

        mockMvc.perform(post("/api/shop/members/likes")
                .header("X-USER-ID", memberId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
    }

    @Test
    void testDeleteBookLikeByMemberId() throws Exception {
        String memberId = "testUser";
        Long customerId = 1L;
        Long bookId = 101L;

        when(memberService.getCustomerIdByMemberLoginId(memberId)).thenReturn(customerId);
        when(bookCommonService.deleteBookLike(bookId, customerId)).thenReturn(true);

        mockMvc.perform(delete("/api/shop/members/likes/{book-id}", bookId)
                .header("X-USER-ID", memberId))
            .andExpect(status().isOk());
    }
}
