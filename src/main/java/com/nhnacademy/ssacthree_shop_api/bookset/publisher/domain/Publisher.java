package com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {

    public Publisher(String publisherName){
        this.publisherName = publisherName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publisherId;

    private String publisherName;

    @Setter
    private boolean publisherIsUsed = true;
}
