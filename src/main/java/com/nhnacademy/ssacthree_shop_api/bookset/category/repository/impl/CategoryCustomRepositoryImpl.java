package com.nhnacademy.ssacthree_shop_api.bookset.category.repository.impl;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.QCategory;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryCustomRepository;


import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {
    private final JPAQueryFactory queryFactory;


    /**
     * 특정 상위 카테고리에 속한 바로 아래 하위 카테고리들 조회
     *
     * @param parentCategoryId 상위 카테고리 아이디
     * @return 특정 상위 카테고리의 바로 아래 카테고리 리스트
     */
    //"select child from Category child where child.superCategoryId = :parentCategoryId"
    @Override
    public List<Category> findChildCategoryByParentCategoryId(long parentCategoryId) {
        QCategory childCategory = new QCategory("childCategory");
        QCategory parentCategory = new QCategory("parentCategory");
        return queryFactory.select(childCategory)
                .from(childCategory)
                .leftJoin(childCategory.superCategory, parentCategory)
                .where(parentCategory.categoryId.eq(parentCategoryId))
                .fetch();
    }

    /**
     * 특정 하위 카테고리의 부모 카테고리들 조회
     *
     * @param childCategoryId 하위 카테고리 아이디
     * @return 특정 하위 카테고리의 부모 카테고리 리스트
     */
    //select c.superCategory from category c where c.categoryId = : childCategoryId
    @Override
    public List<Category> findParentCategoryByCategoryId(long childCategoryId) {
        QCategory category = new QCategory("childCategory");
        return queryFactory.select(category.superCategory)
                .from(category)
                .where(category.categoryId.eq(childCategoryId))
                .fetch();
    }

    /**
     * 최상위 카테고리에 속하는 최하위 카테고리 조회
     * @param rootCategoryId
     * @return
     */
    // select c3 from category3 c3 left join category2 c2 on c3.superCategory = c2 left join category1 c1 on c2.superCategory = c1 where c1.categoryId = :rootCategoryId
    @Override
    public List<Category> findLeafCategoriesByRootCategoryId(long rootCategoryId) {
        QCategory rootCategory = new QCategory("rootCategory");
        QCategory middleCategory = new QCategory("middleCategory");
        QCategory leafCategory = new QCategory("leafCategory");

        return queryFactory.select(leafCategory)
                .from(leafCategory)
                .leftJoin(middleCategory, leafCategory.superCategory)
                .leftJoin(rootCategory, middleCategory.superCategory)
                .where(rootCategory.categoryId.eq(rootCategoryId))
                .fetch();
    }

    /**
     *
     * @param leafCategoryId
     * @return
     */
    // select c2.superCategory from category2 c2 left join category3 c3 on c2 = c3.superCategory where c3.categoryId = :leafCategoryId
    @Override
    public List<Category> findRootCategoryByLeafCategoryId(long leafCategoryId) {
        QCategory middleCategory = new QCategory("middleCategory");
        QCategory leafCategory = new QCategory("leafCategory");

        return queryFactory.select(middleCategory.superCategory)
                .from(middleCategory)
                .leftJoin(leafCategory).on(leafCategory.superCategory.eq(middleCategory))
                .where(leafCategory.categoryId.eq(leafCategoryId))
                .fetch();
    }


    @Override
    public List<Category> findCategoryTreeByRootCategoryId(long rootCategoryId) {
        QCategory category = new QCategory("category");



        return List.of();
    }

    /**
     *
     * @param leafCategoryId
     * @return
     */
    @Override
    public List<Category> findCategoryTreeByLeafCategoryId(long leafCategoryId) {
        return List.of();
    }
}
