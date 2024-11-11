package com.nhnacademy.ssacthree_shop_api.elastic.service;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.BookRepository;
import com.nhnacademy.ssacthree_shop_api.bookset.book.repository.impl.BookCustomRepositoryImpl;
import com.nhnacademy.ssacthree_shop_api.elastic.client.ElasticsearchFeignClient;
import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import com.nhnacademy.ssacthree_shop_api.elastic.exception.DataSyncException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 이 클래스는 MySQL DB에서 데이터를 수집하고 Elasticsearch의 Index에 저장하는 동기화 서비스를 제공합니다.
 * 주기적으로 데이터를 동기화하여 검색 성능을 향상시킵니다.
 * 필요한 설정 값
 * 1. fixedRate: 동기화주기
 * 2. readSize: 한번에 읽어올 데이터의 개수
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DataSyncService {

  private final BookRepository bookRepository;      // MySQL에서 책 정보를 가져오는 Repository
  private final BookCustomRepositoryImpl bookCustomRepository;  // 책과 매핑된 각 정보들을 가져오는 Repo
  private final ElasticsearchFeignClient elasticsearchFeignClient;  // Elasticsearch와의 인터페이스

  // 날짜 포맷터 정의 (ISO 8601 형식 elasticsearch 저장 시 변환하여 저장해야합니다.)
  DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

  @Value("${sync.readSize:100}")
  private int readSize;

  // 인덱스 내의 모든 데이터를 삭제하기 위한 동작입니다.
  public void clearIndex() {
    // 모든 문서를 삭제하기 위한 match_all 쿼리
    Map<String, Object> deleteQuery = Map.of("query", Map.of("match_all", Map.of()));

    // Elasticsearch의 모든 문서를 삭제
    elasticsearchFeignClient.deleteAllDocuments(deleteQuery);
  }

  // 데이터 동기화 작업
  @Scheduled(fixedRateString = "${sync.fixedRate:3600000}")  // 동기화 주기 사용
  @Transactional  // 작업 중 에러 발생 시 모든 작업을 취소하도록 하나의 트랜잭션으로 처리합니다.
  public void syncBooksFromMySQLToElasticsearch() {
    if (readSize < 1) {
      log.error("readSize must be greater than 0. Current readSize: {}", readSize);
      throw new IllegalArgumentException("Page size must be greater than 0");
    }

    String startTime = LocalDateTime.now().format(dateFormatter);
    log.info("동기화 작업이 {}에 시작되었습니다.", startTime);  // 동기화 시작 시간 로그

    try {
      clearIndex();
      log.info("Elasticsearch 인덱스가 삭제되었습니다.");

      int page = 0;
      Pageable pageable = PageRequest.of(page, readSize);
      Page<Book> bookPage;

      while (true) {
        bookPage = bookRepository.findAll(pageable);
        List<Book> books = bookPage.getContent();

        if (books.isEmpty()) {
          break;
        }

        books.forEach(book -> {
          try {
            // 작가, 출판사, 태그 등 정보 가져오기
            List<String> authorNames = bookCustomRepository.findAuthorNamesByBookId(book.getBookId());
            String authorName = authorNames.isEmpty() ? "작가미상" : String.join(", ", authorNames);

            String publisherName = bookCustomRepository.findPublisherNameByBookId(book.getBookId());
            if (publisherName == null) {
              publisherName = "출판사 존재하지않음.";
            }

            List<String> tagNames = bookCustomRepository.findTagNamesByBookId(book.getBookId());
            String tags = tagNames.isEmpty() ? null : String.join(", ", tagNames);

            String publicationDateStr = book.getPublicationDate().format(dateFormatter);

            // BookDocument 생성
            BookDocument bookDocument = new BookDocument(
                book.getBookId(),
                book.getBookName(),
                book.getBookIndex(),
                book.getBookInfo(),
                book.getBookIsbn(),
                publicationDateStr,
                book.getRegularPrice(),
                book.getSalePrice(),
                book.getIsPacked(),
                book.getStock(),
                book.getBookThumbnailImageUrl(),
                book.getBookViewCount(),
                book.getBookDiscount(),
                publisherName,
                authorName,
                tags
            );

            // Elasticsearch에 저장
            elasticsearchFeignClient.saveBook(bookDocument);

          } catch (Exception e) {
            log.error("도서 {} 동기화 중 오류 발생: {}", book.getBookId(), e.getMessage());
          }
        });

        log.info("MySQL 데이터가 ElasticSearch와 동기화되었습니다. 동기화된 책 수: {}", books.size());
        page++;
        pageable = PageRequest.of(page, readSize);
        if (!bookPage.hasNext()) { // 더 읽을 책 없으면 종료
          break;
        }
      }
    } catch (Exception e) {
      throw new DataSyncException("MySQL과 Elasticsearch 동기화 중 오류 발생", e);
    }

    String endTime = LocalDateTime.now().format(dateFormatter);
    log.info("동기화 작업이 {}에 종료되었습니다.", endTime);
  }
}
