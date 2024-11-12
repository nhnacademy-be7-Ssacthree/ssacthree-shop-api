package com.nhnacademy.ssacthree_shop_api.bookset.tag.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

    boolean existsByTagName(String tagName);
}
