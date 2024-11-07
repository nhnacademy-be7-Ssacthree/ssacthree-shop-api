package com.nhnacademy.ssacthree_shop_api.bookset.bookcategory.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class BookCategoryId implements Serializable {
    private Long bookId;
    private Long categoryId;


}
