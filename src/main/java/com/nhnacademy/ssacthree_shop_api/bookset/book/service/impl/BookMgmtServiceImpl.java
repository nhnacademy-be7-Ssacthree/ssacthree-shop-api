package com.nhnacademy.ssacthree_shop_api.bookset.book.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.author.repository.AuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.mapper.BookMapper;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.AuthorNotSetException;
import com.nhnacademy.ssacthree_shop_api.bookset.book.exception.BookAlreadyExistsException;
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
import com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.dto.response.TagInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.repository.TagRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookMgmtServiceImpl implements BookMgmtService {


    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final PublisherRepository publisherRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final BookTagRepository bookTagRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final AuthorRepository authorRepository;

    /**
     * 새로운 도서를 저장합니다.
     * @param bookSaveRequest 저장할 도서 정보
     * @return 저장된 도서 정보
     */
    @Override
    public BookInfoResponse saveBook(BookSaveRequest bookSaveRequest) {
        Book book = bookMapper.bookSaveRequestToBook(bookSaveRequest);

        if(bookRepository.findByBookIsbn(book.getBookIsbn()) != null){
            throw new BookAlreadyExistsException(book.getBookIsbn());
        }

        Publisher publisher = publisherRepository.findById(bookSaveRequest.getPublisherId()).orElseThrow(() -> new NotFoundException("해당 출판사가 존재하지 않습니다."));
        book.setPublisher(publisher);
        book.setBookStatus(BookStatus.ON_SALE);

        // 카테고리 설정 개수가 올바른지 체크
        validateCategoryCountLimit(bookSaveRequest);

        // 작가 설정 개수가 올바른지 체크
        validateAuthorCountLimit(bookSaveRequest);

        List<CategoryNameResponse> categories = new ArrayList<>();
        List<TagInfoResponse> tags = new ArrayList<>();
        List<AuthorNameResponse> authors = new ArrayList<>();

        for(Long categoryId : bookSaveRequest.getCategoryIdList()) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("해당 카테고리가 존재하지 않습니다."));
            BookCategory bookCategory = new BookCategory(book, category);
            book.addCategory(bookCategory);
            categories.add(new CategoryNameResponse(category));
        }

        for(Long tagId : bookSaveRequest.getTagIdList()) {
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new NotFoundException("해당 태그가 존재하지 않습니다."));
            BookTag bookTag = new BookTag(book, tag);
            book.addTag(bookTag);
            tags.add(new TagInfoResponse(tag));
        }

        for(Long authorId : bookSaveRequest.getAuthorIdList()) {
            Author author = authorRepository.findById(authorId).orElseThrow(() -> new NotFoundException("해당 작가가 존재하지 않습니다."));
            BookAuthor bookAuthor = new BookAuthor(book, author);
            book.addAuthor(bookAuthor);
            authors.add(new AuthorNameResponse(author));
        }

        bookRepository.save(book);

        return new BookInfoResponse(book);
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
    public BookInfoResponse updateBook(Long bookId, BookSaveRequest bookSaveRequest) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("해당 책이 존재하지 않습니다."));


        return null;
    }

    @Override
    public BookInfoResponse deleteBook(Long bookId) {
        return null;
    }

}
