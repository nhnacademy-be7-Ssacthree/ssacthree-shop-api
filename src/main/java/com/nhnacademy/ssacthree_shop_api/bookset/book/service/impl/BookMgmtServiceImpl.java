package com.nhnacademy.ssacthree_shop_api.bookset.book.service.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.repository.AuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.mapper.BookMapper;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.service.BookMgmtService;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.repository.BookAuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.BookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.repository.BookCategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.repository.BookTagRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.repository.TagRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Publisher publisher = publisherRepository.findById(bookSaveRequest.getPublisherId()).orElseThrow(() -> new NotFoundException("해당 출판사가 존재하지 않습니다."));
        book.setPublisher(publisher);
        book.setBookStatus(BookStatus.ON_SALE);

        Book saveBook = bookRepository.save(book);

        List<String> categoryNameList = new ArrayList<>();
        List<String> tagNameList = new ArrayList<>();
        List<String> authorNameList = new ArrayList<>();

        for(Long categoryId : bookSaveRequest.getCategoryIdList()) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("해당 카테고리가 존재하지 않습니다."));
            BookCategory bookCategory = new BookCategory(book, category);
            bookCategoryRepository.save(bookCategory);
            categoryNameList.add(category.getCategoryName());
        }

        for(Long tagId : bookSaveRequest.getTagIdList()) {
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new NotFoundException("해당 태그가 존재하지 않습니다."));
            BookTag bookTag = new BookTag(book, tag);
            bookTagRepository.save(bookTag);
            tagNameList.add(tag.getTagName());
        }

        for(Long authorId : bookSaveRequest.getAuthorIdList()) {
            Author author = authorRepository.findById(authorId).orElseThrow(() -> new NotFoundException("해당 작가가 존재하지 않습니다."));
            BookAuthor bookAuthor = new BookAuthor(book, author);
            bookAuthorRepository.save(bookAuthor);
            authorNameList.add(author.getAuthorName());
        }


        return new BookInfoResponse(saveBook, categoryNameList, tagNameList, authorNameList);
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
