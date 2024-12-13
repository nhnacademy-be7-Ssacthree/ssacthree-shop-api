package com.nhnacademy.ssacthree_shop_api.orderset.orderdetail.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// orderDetail 에 사용되는 DTO
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderDetailResponse {

  // 주문 정보
  private Long orderId;
  private LocalDate orderedDate;            // 날짜까지만 보여줌. (상세한 시간은 주문내역에 나옴), 단 주문 시간과 결제 시간은 다를 수 있으므로 있어야함.
  private String orderNumber;               // 주문 번호
  private LocalDate deliverySetDate;        // 지정 배송일
  private String orderInvoiceNumber;        // 송장 번호
  private String receiverName;              // 받는 사람
  private String receiverPhoneNumber;       // 받는 사람 전화번호
  private String orderRequest;              // 배송 요청사항
  private String deliveryAddressRoadname;   // 배송 도로명 주소
  private String deliveryAddressDetail;     // 배송 상제 주소
  private String deliveryPostalNumber;      // 우편번호
  private int orderTotalPrice;

  // 배송 정책 조회
  private int deliveryFee;                  // 배송비

  // 주문 상세 조회 -> 리스트형식
  private List<OrderDetailDTO> orderDetailList;

  // 결제 조회
  private Long paymentId;                   // 결제 ID 정확한 명세 조회를 위해
  private LocalDateTime paymentCreatedAt;   // 결제 완료일
  private int paymentAmount;                // 실 결제 금액
  private String paymentKey;                // 결제 승인 번호
  private String paymentStatus;             // enum -> string 변환, 한글화 필요 (PaymentStatusEnum)

  // 결제 -> 결제 타입 조회
  private String paymentTypeName;           // 결제 타입

}
