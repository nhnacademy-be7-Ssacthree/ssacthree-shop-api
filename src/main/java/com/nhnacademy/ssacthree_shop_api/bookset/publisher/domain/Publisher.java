package com.nhnacademy.ssacthree_shop_api.bookset.publisher.domain;

import com.nhnacademy.ssacthree_shop_api.bookset.book.domain.Book;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long publisherId;

    private String publisherName;

    private boolean publisherIsUsed;

    public Publisher(String publisherName){
        this.publisherName = publisherName;
    }

}
