package com.nhnacademy.ssacthree_shop_api.bookset.category.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "categoryId")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @NotNull
    @Size(max = 20)
    @Setter
    private String categoryName;

    @NotNull
    @Setter
    private boolean categoryIsUsed;


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


    //categoryIsUsed에서 getter가 작동을 안 해서 추가함.
    public boolean getCategoryIsUsed() {
        return categoryIsUsed;
    }

}
