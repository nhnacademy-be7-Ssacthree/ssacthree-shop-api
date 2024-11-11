package com.nhnacademy.ssacthree_shop_api.elastic.controller;

import com.nhnacademy.ssacthree_shop_api.elastic.domain.BookDocument;
import com.nhnacademy.ssacthree_shop_api.elastic.service.ElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Search")  // 검색은 Search로 접근
public class ElasticController {

  private final ElasticService elasticService;

  @Autowired
  public ElasticController(ElasticService elasticService) {
    this.elasticService = elasticService;
  }

  /**
   * 검색 결과 페이지
   *
   * @param query  검색 키워드
   * @param page   페이지 번호
   * @param sort   정렬 옵션 (예: 조회수, 최신순 등)
   * @param filters 필터 (출판사, 저자 등)
   * @param model  모델에 검색 결과 추가
   * @return 검색 결과를 표시할 HTML 템플릿 (books.html)
   */
  @GetMapping("/books")
  public String getBooksPage(
      @RequestParam(value = "query", required = false, defaultValue = "") String query,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "sort", required = false) String sort,
      @RequestParam Map<String, String> filters,
      Model model) {

    if (query.isEmpty()) {
      model.addAttribute("errorMessage", "검색어를 입력하세요.");
      return "books";
    }

    if (page < 1) {
      page = 1;
    }

    List<BookDocument> books = elasticService.searchBooks(query, page, sort, filters);
    model.addAttribute("books", books);
    model.addAttribute("query", query);
    model.addAttribute("page", page);
    model.addAttribute("sort", sort);

    return "books";  // books.html 템플릿으로 이동
  }


  
  // 책 이름을 클릭했을 때 넘어가지는 상세 페이지
  @GetMapping("/books/{id}")
  public String getBookDetail(@PathVariable Long id, Model model) {
    BookDocument book = elasticService.getBookById(id);
    if (book == null) {
      model.addAttribute("errorMessage", "해당 도서를 찾을 수 없습니다.");
      return "error"; // error.html 또는 적절한 에러 페이지로 이동
    }
    model.addAttribute("book", book);
    return "bookDetail"; // 상세 보기 템플릿 렌더링
  }
}
