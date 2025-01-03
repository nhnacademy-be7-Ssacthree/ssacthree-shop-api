package com.nhnacademy.ssacthree_shop_api.bookset.book.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.author.exception.AuthorNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.author.repository.AuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.mapper.BookMapper;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookUpdateRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookDeleteResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookSearchResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookUpdateResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.AuthorNotSetException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.BookAlreadyExistsException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.BookNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.CategoryLimitExceededException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.CategoryNotSetException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookMgmtService;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.repository.BookAuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.BookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.repository.BookCategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.repository.BookTagRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.exception.CategoryNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.exception.TagNotFoundException;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.repository.TagRepository;
import jakarta.ws.rs.NotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookMgmtServiceImpl implements BookMgmtService {

    private static final String BOOK_ID_NOT_FOUND_MESSAGE = "해당 책 아이디를 찾을 수 없습니다.";
    private static final String CATEGORY_NOT_FOUND_MESSAGE = "해당 카테고리가 존재하지 않습니다.";
    private static final String TAG_NOT_FOUND_MESSAGE = "해당 태그가 존재하지 않습니다.";
    private static final String AUTHOR_NOT_FOUND_MESSAGE = "해당 작가가 존재하지 않습니다.";
    private static final String BOOK_ISBN_NOT_FOUND_MESSAGE = "해당 ISBN인 책이 존채하지 않습니다.";
    private static final String PUBLISHER_NOT_FOUND_MESSAGE = "해당 출판사가 존재하지 않습니다.";
    private static final int UNIT_ZERO = 0;


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
    public Page<BookSearchResponse> getAllBooks(Pageable pageable) {
        Page<BookSearchResponse> booksPage = bookRepository.findAllBooks(pageable);

        List<BookSearchResponse> updatedBooks = booksPage.getContent().stream()
            .map(this::addAuthorsToBook)
            .toList();

        return new PageImpl<>(updatedBooks, pageable, booksPage.getTotalElements());
    }
    /**
     * 새로운 도서를 저장합니다.
     *
     * @param bookSaveRequest 저장할 도서 정보
     * @return 저장된 도서 정보
     */
    @Override
    public BookInfoResponse saveBook(BookSaveRequest bookSaveRequest) {
        Book book = bookMapper.bookSaveRequestToBook(bookSaveRequest);

        if (bookRepository.findBookByBookIsbn(book.getBookIsbn()).isPresent()) {
            throw new BookAlreadyExistsException(book.getBookIsbn());
        }

        Publisher publisher = publisherRepository.findById(bookSaveRequest.getPublisherId())
            .orElseThrow(() -> new NotFoundException(PUBLISHER_NOT_FOUND_MESSAGE));
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
    public BookUpdateResponse updateBook(BookUpdateRequest bookUpdateRequest) {
        // ISBN으로 책을 찾는다
        Book book = findBookByIsbn(bookUpdateRequest.getBookIsbn());

        // 기본 정보 업데이트
        updateBasicInfo(book, bookUpdateRequest);

        // 상태 업데이트
        updateBookStatus(book, bookUpdateRequest.getBookStatus());

        // 출판사 업데이트
        updatePublisher(book, bookUpdateRequest.getPublisherId());

        // 카테고리 업데이트
        updateCategories(book, bookUpdateRequest.getCategoryIdList());

        // 태그 업데이트
        updateTags(book, bookUpdateRequest.getTagIdList());

        // 작가 업데이트
        updateAuthors(book, bookUpdateRequest.getAuthorIdList());

        // 변경된 책 저장
        bookRepository.save(book);

        // 응답 반환
        return new BookUpdateResponse(book);
    }

    private Book findBookByIsbn(String bookIsbn) {
        return bookRepository.findBookByBookIsbn(bookIsbn)
            .orElseThrow(() -> new BookNotFoundException(BOOK_ISBN_NOT_FOUND_MESSAGE));
    }

    private void updateBasicInfo(Book book, BookUpdateRequest bookUpdateRequest) {
        book.setBookId(bookUpdateRequest.getBookId());

        if (!book.getBookName().equals(bookUpdateRequest.getBookName())) {
            book.setBookName(bookUpdateRequest.getBookName());
        }
        if (!book.getBookIndex().equals(bookUpdateRequest.getBookIndex())) {
            book.setBookIndex(bookUpdateRequest.getBookIndex());
        }
        if (!book.getBookInfo().equals(bookUpdateRequest.getBookInfo())) {
            book.setBookInfo(bookUpdateRequest.getBookInfo());
        }
        if (book.getRegularPrice() != bookUpdateRequest.getRegularPrice()) {
            book.setRegularPrice(bookUpdateRequest.getRegularPrice());
        }
        if (book.getSalePrice() != bookUpdateRequest.getSalePrice()) {
            book.setSalePrice(bookUpdateRequest.getSalePrice());
        }
        if (book.getIsPacked() != bookUpdateRequest.getIsPacked()) {
            book.setIsPacked(bookUpdateRequest.getIsPacked());
        }
        if (book.getStock() != bookUpdateRequest.getStock()) {
            book.setStock(bookUpdateRequest.getStock());
        }
        if (book.getBookViewCount() != bookUpdateRequest.getBookViewCount()) {
            book.setBookViewCount(bookUpdateRequest.getBookViewCount());
        }
        if (book.getBookDiscount() != bookUpdateRequest.getBookDiscount()) {
            book.setBookDiscount(bookUpdateRequest.getBookDiscount());
        }
        if (!book.getBookThumbnailImageUrl().equals(bookUpdateRequest.getBookThumbnailImageUrl())) {
            book.setBookThumbnailImageUrl(bookUpdateRequest.getBookThumbnailImageUrl());
        }
    }

    private void updateBookStatus(Book book, String bookStatus) {
        if (bookStatus != null && !bookStatus.isEmpty()) {
            try {
                BookStatus newStatus = BookStatus.valueOf(bookStatus);
                if (!newStatus.equals(book.getBookStatus())) {
                    book.setBookStatus(newStatus);
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("유효하지 않은 상태 값입니다: " + bookStatus);
            }
        } else {
            throw new IllegalArgumentException("상태 값이 제공되지 않았습니다.");
        }
    }

    private void updatePublisher(Book book, Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
            .orElseThrow(() -> new NotFoundException(PUBLISHER_NOT_FOUND_MESSAGE));
        if (book.getPublisher() == null || !book.getPublisher().equals(publisher)) {
            book.setPublisher(publisher);
        }
    }

    private void updateCategories(Book book, List<Long> categoryIdList) {
        if (categoryIdList == null || categoryIdList.isEmpty()) {
            book.clearCategories();
        } else if (hasCategoryChanges(book.getBookCategories(), categoryIdList)) {
            book.clearCategories();
            for (Long categoryId : categoryIdList) {
                Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CategoryNotFoundException("카테고리가 존재하지 않습니다."));
                book.addCategory(new BookCategory(book, category));
            }
        }
    }

    private void updateTags(Book book, List<Long> tagIdList) {
        if (tagIdList == null || tagIdList.isEmpty()) {
            book.clearTags();
        } else if (hasTagChanges(book.getBookTags(), tagIdList)) {
            book.clearTags();
            for (Long tagId : tagIdList) {
                Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new TagNotFoundException("태그가 존재하지 않습니다."));
                book.addTag(new BookTag(book, tag));
            }
        }
    }

    private void updateAuthors(Book book, List<Long> authorIdList) {
        if (authorIdList == null || authorIdList.isEmpty()) {
            book.clearAuthors();
        } else if (hasAuthorChanges(book.getBookAuthors(), authorIdList)) {
            book.clearAuthors();
            for (Long authorId : authorIdList) {
                Author author = authorRepository.findById(authorId)
                    .orElseThrow(() -> new AuthorNotFoundException("작가가 존재하지 않습니다."));
                book.addAuthor(new BookAuthor(book, author));
            }
        }
    }

    /**
     * 카테고리 설정 개수가 0개이거나 10개를 초과했을 경우 exception
     *
     * @param bookSaveRequest 도서 저장 정보
     */
    public void validateCategoryCountLimit(BookSaveRequest bookSaveRequest) {
        if (bookSaveRequest.getCategoryIdList().isEmpty()) {
            throw new CategoryNotSetException();
        } else if (bookSaveRequest.getCategoryIdList().size() > 10) {
            throw new CategoryLimitExceededException();
        }
    }

    /**
     * 카테고리 설정 개수가 0개이거나 10개를 초과했을 경우 exception
     *
     * @param bookSaveRequest 도서 저장 정보
     */
    public void validateAuthorCountLimit(BookSaveRequest bookSaveRequest) {
        if (bookSaveRequest.getAuthorIdList().isEmpty()) {
            throw new AuthorNotSetException();
        }
    }


    @Override
    public BookInfoResponse getBookById(Long bookId) {
        Book book = bookRepository.findByBookId(bookId)
            .orElseThrow(() -> new BookNotFoundException(BOOK_ID_NOT_FOUND_MESSAGE));

        return bookMapper.bookToBookInfoResponse(book);
    }

    @Override
    public BookDeleteResponse deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new BookNotFoundException(BOOK_ID_NOT_FOUND_MESSAGE));

        book.setBookStatus(BookStatus.DELETE_BOOK);
        book.setStock(UNIT_ZERO);
        bookRepository.save(book);

        // Book의 Author 정보 조회
        List<AuthorNameResponse> authors = book.getBookAuthors().stream()
            .map(bookAuthor -> new AuthorNameResponse(bookAuthor.getAuthor().getAuthorName()))
            .toList();

        // 응답 반환
        BookDeleteResponse response = new BookDeleteResponse(
            book.getBookId(),
            book.getBookName(),
            book.getBookInfo(),
            book.getBookStatus(),
            book.getStock()
        );
        response.setAuthors(authors);
        return response;
    }


    private boolean hasCategoryChanges(Set<BookCategory> existingCategories, List<Long> newCategoryIds) {
        if (newCategoryIds == null || newCategoryIds.isEmpty()) {
            return !existingCategories.isEmpty(); // 새 데이터가 없으면 기존 데이터가 있는지 확인
        }
        Set<Long> existingCategoryIds = existingCategories.stream()
            .map(cat -> cat.getCategory().getCategoryId())
            .collect(Collectors.toSet());
        return !existingCategoryIds.equals(new HashSet<>(newCategoryIds));
    }

    private boolean hasTagChanges(Set<BookTag> existingTags, List<Long> newTagIds) {
        if (newTagIds == null || newTagIds.isEmpty()) {
            return !existingTags.isEmpty();
        }
        Set<Long> existingTagIds = existingTags.stream()
            .map(tag -> tag.getTag().getTagId())
            .collect(Collectors.toSet());
        return !existingTagIds.equals(new HashSet<>(newTagIds));
    }

    private boolean hasAuthorChanges(Set<BookAuthor> existingAuthors, List<Long> newAuthorIds) {
        if (newAuthorIds == null || newAuthorIds.isEmpty()) {
            return !existingAuthors.isEmpty();
        }
        Set<Long> existingAuthorIds = existingAuthors.stream()
            .map(author -> author.getAuthor().getAuthorId())
            .collect(Collectors.toSet());
        return !existingAuthorIds.equals(new HashSet<>(newAuthorIds));
    }


    private BookSearchResponse addAuthorsToBook(BookSearchResponse book) {
        List<AuthorNameResponse> authors = bookRepository.findAuthorsByBookId(book.getBookId());

        book.setAuthors(authors);
        return book;
    }
}
