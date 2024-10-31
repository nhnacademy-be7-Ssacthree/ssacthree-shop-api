package com.nhnacademy.ssacthree_shop_api.bookset.author.domain;

import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
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
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private long authorId;
    private String authorName; 
    private String authorInfo;

    public Author(String authorName, String authorInfo) {
        this.authorName = authorName;
        this.authorInfo = authorInfo;
    }

}
