package com.nhnacademy.ssacthree_shop_api.orderset.packaging.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "packaging")
public class Packaging {
    public Packaging(String name, int price, String imageUrl) {
        this.packagingName = name;
        this.packagingPrice = price;
        this.packagingImageUrl = imageUrl;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "packaging_id")
    private Long id;

    @NotNull
    @Column(name = "packaging_name")
    @Setter
    private String packagingName;

    @NotNull
    @Column(name = "packaging_price")
    @Setter
    private int packagingPrice;

    @Column(name = "packaging_image_url", columnDefinition = "TEXT")
    @Setter
    private String packagingImageUrl;
}
