package com.nhnacademy.ssacthree_shop_api.commons.paging;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequestBuilder {

    // Private constructor to prevent instantiation
    private PageRequestBuilder() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Pageable createPageable(int page, int size, String[] sortParams) {
        Sort sort = Sort.unsorted();

        if (sortParams != null && sortParams.length > 0) {
            for (String sortParam : sortParams) {
                String[] parts = sortParam.split(":"); // "field,direction" 형식 분리
                if (parts.length == 2) {
                    String property = parts[0].trim(); // 정렬 기준 필드
                    String direction = parts[1].trim(); // asc 또는 desc
                    sort = sort.and(Sort.by(Sort.Direction.fromString(direction), property));
                } else if (parts.length == 1) {
                    String property = parts[0].trim();
                    sort = sort.and(Sort.by(Sort.Direction.ASC, property));
                } else {
                    throw new IllegalStateException("잘못된 정렬 설정: " + sortParam +
                        ". 올바른 형식은 '필드명' 또는 '필드명,정렬방향'입니다. (예: bookName 또는 bookName,asc)");
                }
            }
        } else {
            throw new IllegalStateException("잘못된 정렬 설정: 올바른 형식은 '필드명' 또는 '필드명,정렬방향'입니다. (예: bookName 또는 bookName,asc)");
        }

        return PageRequest.of(page, size, sort);
    }
}