package com.nhnacademy.ssacthree_shop_api.memberset.member.repository.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import com.nhnacademy.ssacthree_shop_api.memberset.member.dto.MemberInfoGetResponse;
import com.nhnacademy.ssacthree_shop_api.memberset.member.repository.MemberCustomRepository;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
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
class MemberCustomerRepositoryImplTest {

    private MemberCustomRepository memberCustomerRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private JPAQueryFactory queryFactory;

    @BeforeEach
    public void setUp() {
        memberCustomerRepository = new MemberCustomerRepositoryImpl(queryFactory);
    }

    @Test
    void testGetMemberWithCustomer() {
        // Given
        String memberLoginId = "testLoginId";

        // Create and persist MemberGrade
        MemberGrade memberGrade = new MemberGrade("Gold", true, 0.05f);
        testEntityManager.persist(memberGrade);

        // Create and persist Customer
        Customer customer = new Customer("John Doe", "john@example.com", "1234567890");
        testEntityManager.persist(customer);

        // Create and persist Member
        Member member = new Member(customer, memberLoginId, "password123", "1990-01-01");

        // Set the memberGrade using reflection since there's no setter
        setField(member, "memberGrade", memberGrade);

        testEntityManager.persist(member);

        // Flush and clear the persistence context to ensure entities are fetched from the database
        testEntityManager.flush();
        testEntityManager.clear();

        // When
        MemberInfoGetResponse response = memberCustomerRepository.getMemberWithCustomer(
            memberLoginId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getMemberLoginId()).isEqualTo(memberLoginId);
        assertThat(response.getCustomerName()).isEqualTo("John Doe");
        assertThat(response.getCustomerEmail()).isEqualTo("john@example.com");
        assertThat(response.getCustomerPhoneNumber()).isEqualTo("1234567890");
        assertThat(response.getMemberBirthdate()).isEqualTo("1990-01-01");
        assertThat(response.getMemberGradeName()).isEqualTo("Gold");
        assertThat(response.getMemberGradePointSave()).isEqualTo(0.05f);
    }

   
    private void setField(Object targetObject, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = targetObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(targetObject, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
            return new JPAQueryFactory(entityManager);
        }
    }
}
