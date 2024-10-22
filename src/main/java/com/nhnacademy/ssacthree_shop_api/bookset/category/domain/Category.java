package com.nhnacademy.ssacthree_shop_api.bookset.category.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private long categoryId;

    @NotNull
    @Size(max = 20)
    private String categoryName;


    // 자기 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "super_category_id")
    private Category superCategory;

    @OneToMany(mappedBy = "superCategory", cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();

}
