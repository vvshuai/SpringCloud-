package com.vvs.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Author: vvshuai
 * @Description: 分发目标
 * @Date: Created in 11:13 2020/7/14
 * @Modified By:
 */
@Getter
@AllArgsConstructor
public enum DistributeTarget {

    /** 实际例子 */
    SINGLE("单用户", 1),
    MULTI("多用户", 2);

    /** 分发目标描述 */
    private String description;

    /** 分发目标编码 */
    private Integer code;

    public static DistributeTarget of(Integer code){
        Objects.requireNonNull(code);


        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
