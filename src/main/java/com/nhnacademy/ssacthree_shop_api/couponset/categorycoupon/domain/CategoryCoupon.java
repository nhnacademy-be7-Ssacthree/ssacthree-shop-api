package com.nhnacademy.ssacthree_shop_api.couponset.categorycoupon.domain;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.couponset.coupon.domain.Coupon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCoupon {
    @Id
    @ManyToOne
    @JoinColumn(name="couponId")
    private Coupon coupon;

    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="categoryId")
    private Category category;

}
