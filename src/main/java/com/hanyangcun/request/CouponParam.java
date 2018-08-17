package com.hanyangcun.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponParam {
    private String name;
    private String couponNo;
    private float discountPrice;
}
