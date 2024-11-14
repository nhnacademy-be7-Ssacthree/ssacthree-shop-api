package com.nhnacademy.ssacthree_shop_api.couponset.couponrule.dto;

import com.nhnacademy.ssacthree_shop_api.couponset.couponrule.domain.CouponType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CouponRuleCreateRequest {

    @NotBlank
    @Size(max = 20)
    private String couponRuleName;

    @NotNull
    private CouponType couponType;

    @NotNull
    @Min(0)
    private int couponMinOrderPrice;

    @NotNull
    @Min(0)
    private int maxDiscountPrice;

    @NotNull
    @Min(0)
    private int couponDiscountPrice;

    @AssertTrue(message = "최대 할인 금액은 쿠폰 할인 금액보다 작을 수 없습니다.")
    public boolean isMaxDiscountPriceValid() {
        return maxDiscountPrice >= couponDiscountPrice;
    }

    @AssertTrue(message = "최소 주문 금액은 쿠폰 할인 금액보다 작을 수 없습니다.")
    public boolean isCouponMinOrderPriceValid() {
        return couponMinOrderPrice >= couponDiscountPrice;
    }

    @AssertTrue(message= "최대 할인 금액은 최소 주문 금액보다 클 수 없습니다.")
    public boolean isMaxDiscountPriceValid2() {
        return maxDiscountPrice <= couponMinOrderPrice;
    }
}
