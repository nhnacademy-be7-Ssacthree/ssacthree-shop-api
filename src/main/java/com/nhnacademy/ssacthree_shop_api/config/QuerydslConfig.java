package com.nhnacademy.ssacthree_shop_api.config;


import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // @Entity로 관리되는 JPA 엔터티들의 상태 변화를 감시하는 기능을 킨다.
public class QuerydslConfig {

    @PersistenceContext // EntityManager에 의존성 주입을 담당하는 어노테이션
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em){
        return new JPAQueryFactory(JPQLTemplates.DEFAULT , em);
    }
}
