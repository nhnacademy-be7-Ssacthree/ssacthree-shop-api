package com.nhnacademy.ssacthree_shop_api.memberset.address.domain;

import com.nhnacademy.ssacthree_shop_api.memberset.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Address {

    public Address(Member member, String addressAlias, String addressDetail, String addressRoadname, String addressPostalNumber) {
        this.member = member;
        this.addressAlias = addressAlias;
        this.addressDetail = addressDetail;
        this.addressRoadname = addressRoadname;
        this.addressPostalNumber = addressPostalNumber;
    }

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long registeredAddressId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id")
    private Member member;

    @Size(max = 15)
    private String addressAlias;

    @NotNull
    @Size(max = 30)
    private String addressRoadname;

    @NotNull
    @Size(max = 35)
    private String addressDetail;

    @NotNull
    @Size(min = 5, max = 5)
    private String addressPostalNumber;

}
