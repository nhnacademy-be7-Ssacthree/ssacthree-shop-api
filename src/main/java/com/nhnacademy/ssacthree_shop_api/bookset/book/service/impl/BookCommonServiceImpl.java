package com.nhnacademy.ssacthree_shop_api.bookset.book.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.AuthorNotSetException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.CategoryLimitExceededException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.CategoryNotSetException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookCommonService;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.repository.BookAuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.repository.BookCategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.repository.BookTagRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookCommonServiceImpl implements BookCommonService {
    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final BookTagRepository bookTagRepository;
    private final BookTagRepository bookTagCategoryRepository;
    private final BookAuthorRepository bookAuthorRepository;

    /**
     * 판매 중, 재고 없음 상태 책 모두 조회
     * @param pageable 페이징 처리
     * @return <BookInfoResponse>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookInfoResponse> getAllAvailableBooks(Pageable pageable) {
        return bookRepository.findAllAvailableBooks(pageable);
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

        List<String> categoryNameList = new ArrayList<>();
        List<String> tagNameList = new ArrayList<>();
        List<String> authorNameList = new ArrayList<>();

        for(Category category : bookCategoryRepository.findBookCategoriesByBookId(bookId)){
            categoryNameList.add(category.getCategoryName());
        }

        for(Tag tag : bookTagRepository.findBookTagsByBookId(bookId)){
            tagNameList.add(tag.getTagName());
        }

        for(Author author : bookAuthorRepository.findBookAuthorByBookId(bookId)){
            authorNameList.add(author.getAuthorName());
        }

        if(categoryNameList.isEmpty()){
            throw new CategoryNotSetException();
        }else if(categoryNameList.size() > 10){
            throw new CategoryLimitExceededException();
        }

        if(authorNameList.isEmpty()){
            throw new AuthorNotSetException();
        }

        return new BookInfoResponse(book, categoryNameList, tagNameList, authorNameList);
    }

    /**
     * 최신 출판 순서대로 도서 조회
     * @param pageable 페이징 처리
     * @return Page<BookInfoResponse>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookInfoResponse> getRecentBooks(Pageable pageable) {
        return bookRepository.findRecentBooks(pageable);
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
        return bookRepository.findBooksByBookName(pageable, bookName);
    }

}
