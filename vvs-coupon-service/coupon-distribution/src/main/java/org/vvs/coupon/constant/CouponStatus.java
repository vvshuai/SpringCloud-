package org.vvs.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Author: vvshuai
 * @Description: 用户优惠券状态
 * @Date: Created in 21:45 2020/12/2
 * @Modified By:
 */
@Getter
@AllArgsConstructor
public enum CouponStatus {

    USABLE("可用的", 1),
    USED("已使用的", 2),
    EXPIRED("过期的(未被使用的)", 3);

    /** 状态描述信息 */
    private String description;

    /** 优惠券状态编码 */
    private Integer code;

    /**
     * @Description: 根据code获取到CouponStatus
     * @return: org.vvs.coupon.constant.CouponStatus
     */
    public static CouponStatus of(Integer code) {

        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists"));
    }
}
