package com.nhnacademy.ssacthree_shop_api.bookset.category.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.ssacthree_shop_api.bookset.category.domain.Category;
import com.nhnacademy.ssacthree_shop_api.bookset.category.repository.CategoryCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EntityScan(basePackages = "com.nhnacademy.ssacthree_shop_api")
@ActiveProfiles("test")
class CategoryCustomRepositoryImplTest {

    private CategoryCustomRepository categoryCustomRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private JPAQueryFactory queryFactory;

    @BeforeEach
    public void setUp() {
        categoryCustomRepository = new CategoryCustomRepositoryImpl(queryFactory);
    }

    @Test
    void testFindCategoriesByName() {
        // Given
        Category category1 = new Category();
        category1.setCategoryName("Books");
        category1.setCategoryIsUsed(true);
        testEntityManager.persist(category1);

        Category category2 = new Category();
        category2.setCategoryName("Ebooks");
        category2.setCategoryIsUsed(true);
        testEntityManager.persist(category2);

        Category category3 = new Category();
        category3.setCategoryName("Music");
        category3.setCategoryIsUsed(true);
        testEntityManager.persist(category3);

        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<Category> result = categoryCustomRepository.findCategoriesByName("book");

        // Then
        assertThat(result).hasSize(2);
        assertThat(result)
            .extracting(Category::getCategoryName)
            .containsExactlyInAnyOrder("Books", "Ebooks");
    }

    @Test
    void testFindCategoryPath() {
        // Given
        Category electronics = new Category();
        electronics.setCategoryName("Electronics");
        electronics.setCategoryIsUsed(true);
        testEntityManager.persist(electronics);

        Category computers = new Category();
        computers.setCategoryName("Computers");
        computers.setSuperCategory(electronics);
        computers.setCategoryIsUsed(true);
        testEntityManager.persist(computers);

        Category laptops = new Category();
        laptops.setCategoryName("Laptops");
        laptops.setSuperCategory(computers);
        laptops.setCategoryIsUsed(true);
        testEntityManager.persist(laptops);

        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<Category> path = categoryCustomRepository.findCategoryPath(laptops.getCategoryId());

        // Then
        assertThat(path).hasSize(3);
        assertThat(path)
            .extracting(Category::getCategoryName)
            .containsExactly("Electronics", "Computers", "Laptops");
    }

    @Test
    void testFindAllDescendants() {
        // Given
        Category electronics = new Category();
        electronics.setCategoryName("Electronics");
        electronics.setCategoryIsUsed(true);
        testEntityManager.persist(electronics);

        Category computers = new Category();
        computers.setCategoryName("Computers");
        computers.setSuperCategory(electronics);
        computers.setCategoryIsUsed(true);
        testEntityManager.persist(computers);

        Category laptops = new Category();
        laptops.setCategoryName("Laptops");
        laptops.setSuperCategory(computers);
        laptops.setCategoryIsUsed(true);
        testEntityManager.persist(laptops);

        Category smartphones = new Category();
        smartphones.setCategoryName("Smartphones");
        smartphones.setSuperCategory(electronics);
        smartphones.setCategoryIsUsed(true);
        testEntityManager.persist(smartphones);

        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<Category> descendants = categoryCustomRepository.findAllDescendants(
            electronics.getCategoryId());

        // Then
        assertThat(descendants).hasSize(3);
        assertThat(descendants)
            .extracting(Category::getCategoryName)
            .containsExactlyInAnyOrder("Computers", "Laptops", "Smartphones");
    }

    @Test
    void testFindCategoryNameByCategoryId() {
        // Given
        Category category = new Category();
        category.setCategoryName("Books");
        category.setCategoryIsUsed(true);
        testEntityManager.persist(category);

        testEntityManager.flush();
        testEntityManager.clear();

        // When
        String categoryName = categoryCustomRepository.findCategoryNameByCategoryId(
            category.getCategoryId());

        // Then
        assertThat(categoryName).isEqualTo("Books");
    }

    @Test
    void testFindBySuperCategoryIsNullAndCategoryIsUsed() {
        // Given
        Category category1 = new Category();
        category1.setCategoryName("Books");
        category1.setCategoryIsUsed(true);
        testEntityManager.persist(category1);

        Category category2 = new Category();
        category2.setCategoryName("Electronics");
        category2.setCategoryIsUsed(true);
        testEntityManager.persist(category2);

        Category category3 = new Category();
        category3.setCategoryName("Computers");
        category3.setSuperCategory(category2);
        category3.setCategoryIsUsed(true);
        testEntityManager.persist(category3);

        testEntityManager.flush();
        testEntityManager.clear();

        // When
        List<Category> topCategories = categoryCustomRepository.findBySuperCategoryIsNullAndCategoryIsUsed();

        // Then
        assertThat(topCategories).hasSize(2);
        assertThat(topCategories)
            .extracting(Category::getCategoryName)
            .containsExactlyInAnyOrder("Books", "Electronics");
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
            return new JPAQueryFactory(entityManager);
        }
    }
}
