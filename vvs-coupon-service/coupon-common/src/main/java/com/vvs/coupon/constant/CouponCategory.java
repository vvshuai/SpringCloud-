package com.vvs.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Author: vvshuai
 * @Description: 优惠券分类
 * @Date: Created in 22:43 2020/7/13
 * @Modified By:
 */
@Getter
@AllArgsConstructor
public enum CouponCategory {

    /**@Description: 满减券
     */
    MANJIAN("满减券","001"),
    ZHEKOU("折扣券", "002"),
    LIJIAN("立减券", "003");

    /** 优惠券描述（分类）*/
    private String description;
    /** 优惠券分类编码*/
    private String code;

    public static CouponCategory of(String code){
        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(()-> new IllegalArgumentException(code + " not exist"));
    }
}
