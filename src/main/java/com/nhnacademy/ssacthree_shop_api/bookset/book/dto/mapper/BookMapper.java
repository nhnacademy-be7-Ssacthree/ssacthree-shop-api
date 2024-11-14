package com.nhnacademy.ssacthree_shop_api.bookset.book.dto.mapper;

import com.nhnacademy.ssacthree_shop_api.bookset.author.domain.Author;
import com.nhnacademy.ssacthree_shop_api.bookset.author.repository.AuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.BookStatus;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.converter.BookStatusConverter;
import com.nhnacademy.ssacthree_shop_api.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.repository.BookAuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.BookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherRepository;

import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.repository.TagRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Component
@RequiredArgsConstructor
public class BookMapper {

    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;

    public Book bookSaveRequestToBook(BookSaveRequest bookSaveRequest) {
        BookStatusConverter converter = new BookStatusConverter();

        Book book = new Book();
        book.setBookName(bookSaveRequest.getBookName());
        book.setBookIndex(bookSaveRequest.getBookIndex());
        book.setBookInfo(bookSaveRequest.getBookInfo());
        book.setBookIsbn(bookSaveRequest.getBookIsbn());

        LocalDate publicationDate = bookSaveRequest.getPublicationDate();
        LocalDateTime publicationDateTime = publicationDate.atStartOfDay(); // 시간은 00:00:00으로 설정
        book.setPublicationDate(publicationDateTime); // 변환된 LocalDateTime 설정

        book.setRegularPrice(bookSaveRequest.getRegularPrice());
        book.setSalePrice(bookSaveRequest.getSalePrice());
        book.setIsPacked(bookSaveRequest.getIsPacked());
        book.setStock(bookSaveRequest.getStock());
        book.setBookThumbnailImageUrl(bookSaveRequest.getBookThumbnailImageUrl());
        book.setBookViewCount(bookSaveRequest.getBookViewCount());
        book.setBookDiscount(bookSaveRequest.getBookDiscount());
        //todo: book 등록할 때 상태를 직접 설정할지, 아니면 무조건 "판매 중"으로 할지 정해야함. 현재는 무조건 "판매 중"
        book.setBookStatus(BookStatus.ON_SALE);

        Publisher publisher = publisherRepository.findById(bookSaveRequest.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));
        book.setPublisher(publisher);

        // Category 설정
        List<Category> categories = categoryRepository.findAllById(bookSaveRequest.getCategoryIdList());
        categories.forEach(category -> book.addCategory(new BookCategory(book, category)));

        // Author 설정
        List<Author> authors = authorRepository.findAllById(bookSaveRequest.getAuthorIdList());
        authors.forEach(author -> book.addAuthor(new BookAuthor(book, author)));

        // Tag 설정
        List<Tag> tags = tagRepository.findAllById(bookSaveRequest.getTagIdList());
        tags.forEach(tag -> book.addTag(new BookTag(book, tag)));

        return book;
    }

}
