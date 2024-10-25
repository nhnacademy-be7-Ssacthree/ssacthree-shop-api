package com.nhnacademy.ssacthree_shop_api.bookset.category.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private long categoryId;

    @NotNull
    @Size(max = 20)
    @Setter
    private String categoryName;

    @NotNull
    @Setter
    @Column(columnDefinition = "boolean default false")
    private boolean categoryIsUsed;
    //todo: H2에서는 "boolean default false" 이거 괜찮지만
    // MySQL에서는 "TINYINT(1) DEFAULT 0" 어떡하죠?
    // 방법3: @Column private boolean categoryIsUsed = false;


    // 자기 참조
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "super_category_id")
    private Category superCategory;

    @OneToMany(mappedBy = "superCategory", cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();

    public Category(String categoryName, Category superCategory) {
        this.categoryName = categoryName;
        this.superCategory = superCategory;
    }


    //todo: categoryIsUsed에서 getter가 작동을 안 해서 임시로 추가함.
    public boolean getCategoryIsUsed() {
        return categoryIsUsed;
    }

}
