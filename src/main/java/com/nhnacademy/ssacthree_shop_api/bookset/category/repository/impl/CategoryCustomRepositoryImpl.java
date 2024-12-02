package com.nhnacademy.ssacthree_shop_api.bookset.category.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.QCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryCustomRepository;


import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    private static final QCategory qCategory = QCategory.category;

    private BooleanExpression isUsedCategory(){
        return qCategory.categoryIsUsed.eq(true);
    }

    @Override
    public List<Category> findCategoriesByName(String name) {
        // 주어진 이름을 포함하는 카테고리를 검색
        return queryFactory.selectFrom(qCategory)
                .where(qCategory.categoryName.containsIgnoreCase(name).and(isUsedCategory()))
                .fetch();
    }

    @Override
    public List<Category> findCategoryPath(Long categoryId) {
        // 주어진 카테고리부터 최상위 카테고리까지의 경로를 조회
        Category current = queryFactory.selectFrom(qCategory)
                .where(qCategory.categoryId.eq(categoryId).and(isUsedCategory()))
                .fetchOne();

        List<Category> path = new ArrayList<>();
        while (current != null) {
            path.add(0, current);
            current = current.getSuperCategory();
        }
        return path;
    }

    @Override
    public List<Category> findAllDescendants(Long categoryId) {
        // 특정 카테고리의 모든 하위 카테고리(자식 및 자손)를 재귀적으로 조회합니다.
        List<Category> descendants = new ArrayList<>();
        addDescendants(categoryId, descendants);
        return descendants;
    }

    private void addDescendants(Long categoryId, List<Category> descendants) {
        List<Category> children = queryFactory.selectFrom(qCategory)
                .where(qCategory.superCategory.categoryId.eq(categoryId).and(isUsedCategory()))
                .fetch();

        descendants.addAll(children);
        for (Category child : children) {
            addDescendants(child.getCategoryId(), descendants); // 빈리스트의 경우 재귀 종료
        }
    }

    @Override
    public String findCategoryNameByCategoryId(Long categoryId) {
        return queryFactory.select(qCategory.categoryName)
                .from(qCategory)
                .where(qCategory.categoryId.eq(categoryId).and(isUsedCategory()))
                .fetchOne();
    }
}
