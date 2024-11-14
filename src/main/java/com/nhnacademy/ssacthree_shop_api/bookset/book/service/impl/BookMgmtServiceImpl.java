package com.nhnacademy.ssacthree_shop_api.bookset.book.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.author.exception.AuthorNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.author.repository.AuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.mapper.BookMapper;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookBaseResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookSearchResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.*;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookMgmtService;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.repository.BookAuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.BookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.repository.BookCategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.repository.BookTagRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.exception.CategoryNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.exception.TagNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.repository.TagRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookMgmtServiceImpl implements BookMgmtService {

    public static final String BOOK_ID_NOT_FOUND_MESSAGE = "해당 책 아이디를 찾을 수 없습니다.";
    public static final String CATEGORY_NOT_FOUND_MESSAGE  = "해당 카테고리가 존재하지 않습니다.";
    public static final String TAG_NOT_FOUND_MESSAGE = "해당 태그가 존재하지 않습니다.";
    public static final String AUTHOR_NOT_FOUND_MESSAGE = "해당 작가가 존재하지 않습니다.";


    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final PublisherRepository publisherRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final BookTagRepository bookTagRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final AuthorRepository authorRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<BookSearchResponse> getAllBooks(Pageable pageable){
        return bookRepository.findAllBooks(pageable);
    }

    /**
     * 새로운 도서를 저장합니다.
     * @param bookSaveRequest 저장할 도서 정보
     * @return 저장된 도서 정보
     */
    @Override
    public BookInfoResponse saveBook(BookSaveRequest bookSaveRequest) {
        Book book = bookMapper.bookSaveRequestToBook(bookSaveRequest);

         if(bookRepository.findBookByBookIsbnForAdmin(book.getBookIsbn()) != null){
            throw new BookAlreadyExistsException(book.getBookIsbn());
        }

        Publisher publisher = publisherRepository.findById(bookSaveRequest.getPublisherId()).orElseThrow( () -> new NotFoundException("해당 출판사가 존재하지 않습니다."));
        book.setPublisher(publisher);

        // 카테고리 설정 개수가 올바른지 체크
        validateCategoryCountLimit(bookSaveRequest);

        // 작가 설정 개수가 올바른지 체크
        validateAuthorCountLimit(bookSaveRequest);

        // 카테고리 업데이트
        if (hasCategoryChanges(book.getBookCategories(), bookSaveRequest.getCategoryIdList())) {
            book.clearCategories();
            for (Long categoryId : bookSaveRequest.getCategoryIdList()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND_MESSAGE));
                book.addCategory(new BookCategory(book, category));
            }
        }

        // 태그 업데이트
        if (hasTagChanges(book.getBookTags(), bookSaveRequest.getTagIdList())) {
            book.clearTags();
            for (Long tagId : bookSaveRequest.getTagIdList()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new TagNotFoundException(TAG_NOT_FOUND_MESSAGE));
                book.addTag(new BookTag(book, tag));
            }
        }

        // 작가 업데이트
        if (hasAuthorChanges(book.getBookAuthors(), bookSaveRequest.getAuthorIdList())) {
            book.clearAuthors();
            for (Long authorId : bookSaveRequest.getAuthorIdList()) {
                Author author = authorRepository.findById(authorId)
                        .orElseThrow(() -> new AuthorNotFoundException(AUTHOR_NOT_FOUND_MESSAGE));
                book.addAuthor(new BookAuthor(book, author));
            }
        }
        bookRepository.save(book);

        return new BookInfoResponse(book);
    }

    @Override
    public BookInfoResponse updateBook(Long bookId, BookSaveRequest bookSaveRequest) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(BOOK_ID_NOT_FOUND_MESSAGE));

        if (!book.getBookIsbn().equals(bookSaveRequest.getBookIsbn()) &&
                bookRepository.findBookByBookIsbnForAdmin(bookSaveRequest.getBookIsbn()) != null) {
            throw new BookAlreadyExistsException(bookSaveRequest.getBookIsbn());
        }

        return saveBook(bookSaveRequest);

    }

    /**
     * 카테고리 설정 개수가 0개이거나 10개를 초과했을 경우 exception
     * @param bookSaveRequest 도서 저장 정보
     */
    public void validateCategoryCountLimit(BookSaveRequest bookSaveRequest) {
        if(bookSaveRequest.getCategoryIdList().isEmpty()){
            throw new CategoryNotSetException();
        }else if(bookSaveRequest.getCategoryIdList().size() > 10){
            throw new CategoryLimitExceededException();
        }
    }

    /**
     * 카테고리 설정 개수가 0개이거나 10개를 초과했을 경우 exception
     * @param bookSaveRequest 도서 저장 정보
     */
    public void validateAuthorCountLimit(BookSaveRequest bookSaveRequest) {
        if(bookSaveRequest.getAuthorIdList().isEmpty()){
            throw new AuthorNotSetException();
        }
    }


    @Override
    public Page<BookInfoResponse> getBookById(Long bookId, Pageable pageable) {
         Page<BookInfoResponse> bookBaseResponsePage = bookRepository.findBooksByBookId(bookId, pageable);

        return null;
    }

    @Override
    public BookInfoResponse deleteBook(Long bookId, BookSaveRequest bookSaveRequest) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(BOOK_ID_NOT_FOUND_MESSAGE));

        // 책 상태 업데이트, 기존 책에서 필요한 부분만 변경
        book.setBookStatus(BookStatus.DELETE_BOOK);  // 예시로 'ON_SALE'로 상태 변경

        return saveBook(bookSaveRequest);
    }


    private boolean hasCategoryChanges(Set<BookCategory> existingCategories, List<Long> newCategoryIds) {
        Set<Long> existingCategoryIds = existingCategories.stream()
                .map(cat -> cat.getCategory().getCategoryId())
                .collect(Collectors.toSet());
        return !existingCategoryIds.equals(new HashSet<>(newCategoryIds));
    }

    private boolean hasTagChanges(Set<BookTag> existingTags, List<Long> newTagIds) {
        Set<Long> existingTagIds = existingTags.stream()
                .map(tag -> tag.getTag().getTagId())
                .collect(Collectors.toSet());
        return !existingTagIds.equals(new HashSet<>(newTagIds));
    }

    private boolean hasAuthorChanges(Set<BookAuthor> existingAuthors, List<Long> newAuthorIds) {
        Set<Long> existingAuthorIds = existingAuthors.stream()
                .map(author -> author.getAuthor().getAuthorId())
                .collect(Collectors.toSet());
        return !existingAuthorIds.equals(new HashSet<>(newAuthorIds));
    }

}
