package com.nhnacademy.ssacthree_shop_api.bookset.book.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookBaseResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookCommonService;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.repository.BookAuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.repository.BookCategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.repository.BookTagRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.dto.PublisherNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookCommonServiceImpl implements BookCommonService {
    private final BookRepository bookRepository;

    /**
     * BookInfoResponse에 출판사, 카테고리, 태그, 작가 정보를 주입해주기 위한 메소드
     * @param bookBaseResponse 도서 기본 정보(아직 출판사, 카테고리, 태그, 작가 정보 비어 있음)
     */
    private BookInfoResponse addAssociatedDataToBookInfoResponse(BookBaseResponse bookBaseResponse){

        BookInfoResponse bookInfoResponse = new BookInfoResponse(bookBaseResponse);

        // 카테고리 설정
        List<CategoryNameResponse> categories = bookRepository.findCategoriesByBookId(bookInfoResponse.getBookId());
        bookInfoResponse.setCategories(categories);

        // 태그 설정
        List<TagInfoResponse> tags = bookRepository.findTagsByBookId(bookInfoResponse.getBookId());
        bookInfoResponse.setTags(tags);

        // 작가 설정
        List<AuthorNameResponse> authors = bookRepository.findAuthorsByBookId(bookInfoResponse.getBookId());
        bookInfoResponse.setAuthors(authors);

        return bookInfoResponse;
    }

    /**
     * 판매 중, 재고 없음 상태 책 모두 조회
     * @param pageable 페이징 처리
     * @return <BookInfoResponse>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookInfoResponse> getAllAvailableBooks(Pageable pageable) {
        Page<BookBaseResponse> booksPage = bookRepository.findAllAvailableBooks(pageable);

        List<BookInfoResponse> bookInfoResponses = booksPage.getContent().stream()
                .map(this::addAssociatedDataToBookInfoResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(bookInfoResponses, pageable, booksPage.getTotalElements());
    }

    /**
     * 도서 id로 도서 검색
     * @param bookId
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public BookInfoResponse getBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException("해당 도서가 존재하지 않습니다."));
        return new BookInfoResponse(book);
    }

    /**
     * 도서 이름으로 도서를 검색합니다.
     * @param bookName 도서 이름
     * @param pageable 페이징 처리
     * @return Page<BookInfoResponse>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookInfoResponse> getBooksByBookName(Pageable pageable, String bookName) {
        Page<BookBaseResponse> booksPage =  bookRepository.findBooksByBookName(pageable, bookName);

        List<BookInfoResponse> bookInfoResponses = booksPage.getContent().stream()
                .map(this::addAssociatedDataToBookInfoResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(bookInfoResponses, pageable, booksPage.getTotalElements());
    }

    /**
     * 도서 ISBN으로 도서를 검색합니다.
     * @param isbn 도서 ISBN
     * @return 해당 ISBN을 가진 도서
     */
    @Override
    public BookInfoResponse getBooksByBookIsbn(String isbn) {

        return addAssociatedDataToBookInfoResponse(bookRepository.findByBookIsbn(isbn));
    }

    @Override
    public Page<BookInfoResponse> getBooksByAuthorId(Pageable pageable, Long authorId) {
        Page<BookBaseResponse> booksPage =  bookRepository.findBooksByAuthorId(authorId, pageable);

        List<BookInfoResponse> bookInfoResponses = booksPage.getContent().stream()
                .map(this::addAssociatedDataToBookInfoResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(bookInfoResponses, pageable, booksPage.getTotalElements());
    }

    /**
     * 도서 아이디로 카테고리 리스트 가져오기
     * @param bookId 도서 아이디
     * @return 카테고리 리스트
     */
    @Override
    public List<CategoryNameResponse> getCategoriesByBookId(Long bookId) {
        if(bookRepository.findById(bookId).isEmpty()) {
            throw new NotFoundException("해당 도서가 존재하지 않습니다.");
        }

        return bookRepository.findCategoriesByBookId(bookId);
    }

}
