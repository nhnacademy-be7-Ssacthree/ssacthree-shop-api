package com.nhnacademy.ssacthree_shop_api.bookset.book.domain;


import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.converter.BookStatusConverter;
import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
import com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain.BookCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.booktag.domain.BookTag;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain.Publisher;
import jakarta.persistence.*; // jakarta -> javax?
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import lombok.Setter;

@Entity
@Table(name = "book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId; // 도서 아이디

    private String bookName; // 도서 이름

    private String bookIndex; // 도서 목차

    private String bookInfo; // 도서 설명

    private String bookIsbn; // 도서 고유 번호

    private LocalDateTime publicationDate; //출판 일시

//    @Pattern(regexp = "\\d+", message = "가격은 숫자로 입력해주세요")
    private int regularPrice; // 정가

//    @Pattern(regexp = "\\d+", message = "가격은 숫자로 입력해주세요")
    private int salePrice; // 판매가

    private boolean isPacked; //포장 가능 여부

    private int stock; // 재고

    private String bookThumbnailImageUrl; // 도서 썸네일 이미지

    private int bookViewCount; // 도서 조회수

    private int bookDiscount; // 할인율

    @Convert(converter = BookStatusConverter.class)
    private BookStatus bookStatus; // 도서 상태

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private  Publisher publisher;
    //orphanRemoval = true 도서 수정 시 연결된 자식 엔티티가 부모와의 관계가 끊어졌을 때 JPA가 이를 자동으로 삭제
    @OneToMany(mappedBy="book", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookCategory> bookCategories = new HashSet<>();

    @OneToMany(mappedBy="book", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookAuthor> bookAuthors = new HashSet<>();

    @OneToMany(mappedBy="book", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BookTag> bookTags = new HashSet<>();

    public void addCategory(BookCategory bookCategory) {
        bookCategories.add(bookCategory);
    }

    public void addTag(BookTag bookTag) {
        bookTags.add(bookTag);
    }

    public void addAuthor(BookAuthor bookAuthor) {
        bookAuthors.add(bookAuthor);
    }

    public boolean getIsPacked() {
        return isPacked;
    }

    public void setIsPacked(boolean isPacked) {
        this.isPacked = isPacked;
    }

    public void clearCategories() {
        for (BookCategory bookCategory : bookCategories) {
            bookCategory.setBook(null); // 부모 관계 끊기
        }
        bookCategories.clear(); // Set 비우기
    }

    public void clearTags() {
        for (BookTag bookTag : bookTags) {
            bookTag.setBook(null);
        }
        bookTags.clear();
    }

    public void clearAuthors() {
        for (BookAuthor bookAuthor : bookAuthors) {
            bookAuthor.setBook(null);
        }
        bookAuthors.clear();
    }


}
