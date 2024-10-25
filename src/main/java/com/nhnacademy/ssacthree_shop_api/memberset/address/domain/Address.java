package com.nhnacademy.ssacthree_shop_api.memberset.address.domain;

import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Address {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registered_address_id")
    private long addressId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id")
    private Member member;

    @Null
    @Size(max = 15)
    private String addressAlias;

    @NotNull
    @Size(max = 35)
    private String addressDetail;

    @NotNull
    @Size(min = 5, max = 5)
    private String addressPostalNumber;

}
