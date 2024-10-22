package com.nhnacademy.ssacthree_shop_api.bookset.author.domain;

import com.nhnacademy.ssacthree_shop_api.bookset.bookauthor.domain.BookAuthor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private long authorId;

    @Size(max = 20)
    @NotNull
    private String authorName;

    @NotNull
    @Size(max = 255)
    private String authorInfo;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<BookAuthor> bookAuthors;

}
