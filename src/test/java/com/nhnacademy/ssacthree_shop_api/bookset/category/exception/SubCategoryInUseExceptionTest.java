package com.nhnacademy.ssacthree_shop_api.bookset.category.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubCategoryInUseExceptionTest {

  @Test
  void testSubCategoryInUseException() {
    // 예외가 발생할 카테고리 ID를 설정
    Long categoryId = 123L;

    // SubCategoryInUseException이 발생하는지 테스트
    SubCategoryInUseException exception = assertThrows(SubCategoryInUseException.class, () -> {
      throw new SubCategoryInUseException(categoryId); // 예외 던지기
    });

    // 예외 메시지 검증
    String expectedMessage = "카테고리 ID " + categoryId + "에 사용 중인 하위 카테고리가 있어 사용 여부를 삭제할 수 없습니다.";
    String actualMessage = exception.getMessage();

    // 예외 메시지가 예상한 메시지와 일치하는지 확인
    assertEquals(expectedMessage, actualMessage);
  }
}

