package com.nhnacademy.ssacthree_shop_api.memberset.pointrefund.domain;

import com.nhnacademy.ssacthree_shop_api.memberset.pointhistory.domain.PointHistory;
import com.nhnacademy.ssacthree_shop_api.orderset.refund.domain.Refund;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name="point_refund")
public class PointRefund {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="point_history_id")
    private PointHistory pointHistory;

    @NotNull
    @OneToOne
    @JoinColumn(name = "refund_id")
    private Refund refund;
}
