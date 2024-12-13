package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


// 주문 상세 정보를 리스트로 담기 위한 DTO 입니다. (OrderDetailResponse 에 사용 됨)

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderDetailDTO {
  private Long bookId;  // 클릭 시 해당 책 조회 하는 경우가 있을수도
  private String bookName;
  private String bookThumbnailImageUrl;
  private int quantity;
  private int bookPriceAtOrder;
//  private MemberCoupon memberCoupon = null; // 회원이 아니면 쿠폰 정보도 없음 or 쿠폰 사용 안했을 때
}
