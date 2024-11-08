package com.nhnacademy.ssacthree_shop_api.elastic.service;

import com.nhnacademy.ssacthree_shop_api.bookset.author.repository.AuthorRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.publisher.repository.PublisherRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.tag.repository.TagRepository;
import com.nhnacademy.ssacthree_shop_api.elastic.client.ElasticsearchFeignClient;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MySQLToElasticsearchSyncService {

  private final BookRepository bookRepository;  // MySQL에서 책 정보를 가져오는 Repository

  private final AuthorRepository authorRepository;  // 저자 정보를 가져오는 Repository
  private final PublisherRepository publisherRepository;  // 출판사 정보를 가져오는 Repository
  private final TagRepository tagRepository;  // 태그 정보를 가져오는 Repository

  private final ElasticsearchFeignClient elasticsearchFeignClient;  // Elasticsearch와의 인터페이스

  @Value("${sync.fixedRate:3600000}")
  private long fixedRate;  // 동기화 주기 (기본값: 1시간)

  @Scheduled(fixedRateString = "#{@mySQLToElasticsearchSyncService.fixedRate}")  // 동기화 주기 사용
  public void syncBooksFromMySQLToElasticsearch() {
    try {
      // 1. MySQL에서 모든 책 데이터를 가져옴
      List<Book> books = bookRepository.findAll();

      // 2. Book 데이터를 BookDocument로 변환하여 Elasticsearch에 저장
      books.forEach(book -> {
        String authorName = authorRepository.findById(book.getAuthorId())
            .map(author -> author.getAuthorName()).orElse("Unknown Author");
        String publisherName = publisherRepository.findById(book.getPublisher().getPublisherId())
            .map(publisher -> publisher.getPublisherName()).orElse("Unknown Publisher");
        String tags = String.join(", ", tagRepository.findTagsByBookId(book.getBookId()));

        BookDocument bookDocument = new BookDocument(
            book.getBookId(),
            book.getBookName(),
            book.getBookIndex(),
            book.getBookInfo(),
            book.getBookIsbn(),
            book.getPublicationDate().toString(),
            book.getRegularPrice(),
            book.getSalePrice(),
            book.isPacked(),
            book.getStock(),
            book.getBookThumbnailImageUrl(),
            book.getBookViewCount(),
            book.getBookDiscount(),
            publisherName,
            authorName,
            tags
        );
        elasticsearchFeignClient.saveBook(bookDocument);
      });

      System.out.println("[INFO] MySQL 데이터가 Elasticsearch와 동기화되었습니다. 동기화된 책 수: " + books.size());
    } catch (Exception e) {
      System.err.println("[ERROR] MySQL과 Elasticsearch 동기화 중 오류 발생: " + e.getMessage());
    }
  }
}
