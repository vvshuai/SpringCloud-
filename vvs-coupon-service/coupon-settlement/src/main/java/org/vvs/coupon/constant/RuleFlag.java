package org.vvs.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: vvshuai
 * @Description: 规则类型枚举定义
 * @Date: Created in 21:37 2020/12/5
 * @Modified By:
 */
@Getter
@AllArgsConstructor
public enum RuleFlag {

    // 单类别优惠券定义
    MANJIAN("满减券的计算规则"),
    ZHEKOU("折扣券的计算规则"),
    LIJIAN("立减券的计算规则"),

    // 多类别优惠券定义
    MANJIAN_ZHEKOU("满减券加折扣券的计算规则");

    // TODO 增加多类别

    /** 规则的描述 */
    private String description;

}
