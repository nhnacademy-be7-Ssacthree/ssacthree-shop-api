package com.nhnacademy.ssacthree_shop_api.member_set.member.domain;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.member_set.member_grade.domain.MemberGrade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Member {


    @Id
    @NotNull
    private long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @NotNull
    private Customer customer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_grade_id")
    @NotNull
    private MemberGrade memberGrade;

    @NotNull
    @Size(min = 8, max = 20)
    private String loginId;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    @Size(min=8, max=8)
    private String birthdate;

    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @Null
    private LocalDateTime last_login_at;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MemberStatus status = MemberStatus.ACTIVE;

    @NotNull
    private int point = 0;


}
