package com.nhnacademy.ssacthree_shop_api.bookset.category.repository;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.impl.CategoryCustomRepositoryImpl;
import com.nhnacademy.ssacthree_shop_api.config.QuerydslConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({CategoryCustomRepositoryImpl.class, QuerydslConfig.class}) // 실제 구현체 등록
class CategoryCustomRepositoryTest {

    @Autowired
    @Qualifier("categoryCustomRepositoryImpl") // 주입할 빈을 명확히 지정
    private CategoryCustomRepository categoryCustomRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        // 기본 테스트 데이터 설정
        Category rootCategory = new Category();
        rootCategory.setCategoryName("루트카테고리");
        rootCategory.setCategoryIsUsed(true);
        entityManager.persist(rootCategory);

        Category childCategory1 = new Category();
        childCategory1.setCategoryName("자식카테고리1");
        childCategory1.setSuperCategory(rootCategory);
        childCategory1.setCategoryIsUsed(true);
        entityManager.persist(childCategory1);

        Category childCategory2 = new Category();
        childCategory2.setCategoryName("자식카테고리2");
        childCategory2.setSuperCategory(rootCategory);
        childCategory2.setCategoryIsUsed(true);
        entityManager.persist(childCategory2);

        Category grandchildCategory = new Category();
        grandchildCategory.setCategoryName("손자카테고리");
        grandchildCategory.setSuperCategory(childCategory1);
        grandchildCategory.setCategoryIsUsed(true);
        entityManager.persist(grandchildCategory);

        entityManager.flush();
    }

    /**
     * findCategoriesByName 테스트
     */
    @Test
    void findCategoriesByName() {
        List<Category> result = categoryCustomRepository.findCategoriesByName("카테고리");
        assertThat(result).hasSize(4); // "카테고리"가 이름에 포함된 4개의 카테고리가 검색되어야 함
    }

    /**
     * findCategoryPath 테스트
     */
    @Test
    void findCategoryPath() {
        Category targetCategory = categoryCustomRepository.findCategoriesByName("손자카테고리").get(0);
        List<Category> path = categoryCustomRepository.findCategoryPath(targetCategory.getCategoryId());

        assertThat(path).hasSize(3); // 루트 -> 자식 -> 손자
        assertThat(path.get(0).getCategoryName()).isEqualTo("루트카테고리");
        assertThat(path.get(1).getCategoryName()).isEqualTo("자식카테고리1");
        assertThat(path.get(2).getCategoryName()).isEqualTo("손자카테고리");
    }

    /**
     * findAllDescendants 테스트
     */
    @Test
    void findAllDescendants() {
        Category rootCategory = categoryCustomRepository.findCategoriesByName("루트카테고리").get(0);
        List<Category> descendants = categoryCustomRepository.findAllDescendants(rootCategory.getCategoryId());

        assertThat(descendants).hasSize(3); // 자식 2명, 손자 1명
        assertThat(descendants.stream().map(Category::getCategoryName)).containsExactlyInAnyOrder("자식카테고리1", "자식카테고리2", "손자카테고리");
    }

    /**
     * findCategoryNameByCategoryId 테스트
     */
    @Test
    void findCategoryNameByCategoryId() {
        Category targetCategory = categoryCustomRepository.findCategoriesByName("손자카테고리").get(0);
        String categoryName = categoryCustomRepository.findCategoryNameByCategoryId(targetCategory.getCategoryId());

        assertThat(categoryName).isEqualTo("손자카테고리");
    }
}
