package com.nhnacademy.ssacthree_shop_api.couponset.bookcoupon.domain;

import com.nhnacademy.ssacthree_shop_api.couponset.categorycoupon.domain.CategoryCoupon;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.CouponEffectivePeriodUnit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CategoryCouponTest {

    @Test
    void testAllArgsConstructorWorks() {
        // Given
        Coupon coupon = new Coupon("Coupon Name", "Coupon Description", 30,
                CouponEffectivePeriodUnit.DAY, null, null);
        Category category = new Category("Category Name", null);

        // When
        CategoryCoupon categoryCoupon = new CategoryCoupon(coupon, category);

        // Then
        assertThat(categoryCoupon.getCoupon()).isEqualTo(coupon);
        assertThat(categoryCoupon.getCategory()).isEqualTo(category);
    }

    @Test
    void testNoArgsConstructorCreatesInstance() {
        // 기본 생성자를 사용하여 객체 생성
        CategoryCoupon categoryCoupon = new CategoryCoupon();

        // 객체가 null이 아님을 확인
        assertNotNull(categoryCoupon, "기본 생성자를 통해 객체가 생성되어야 합니다.");
    }
}
