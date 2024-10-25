package com.nhnacademy.ssacthree_shop_api.memberset.member.domain;

import com.nhnacademy.ssacthree_shop_api.customer.domain.Customer;
import com.nhnacademy.ssacthree_shop_api.memberset.membergrade.domain.MemberGrade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "member")
public class Member {

    public Member(
        Customer customer,
        String loginId,
        String password,
        String birthdate
    ) {
        this.customer = customer;
        this.loginId = loginId;
        this.password = password;
        this.birthdate = birthdate;
    }

    @Id
    private long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_grade_id")
    @Setter
    private MemberGrade memberGrade;

    @Column(name = "member_login_id")
    private String loginId;

    @Column(name = "member_password")
    private String password;

    @Column(name = "member_birthdate")
    private String birthdate;


    @Column(name = "member_created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "member_last_login_at")
    private LocalDateTime last_login_at;


    @Column(name = "member_status")
    @Enumerated(EnumType.STRING)
    private MemberStatus status = MemberStatus.ACTIVE;

    @Column(name = "member_point")
    private int point = 0;


}
