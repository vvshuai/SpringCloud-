package org.vvs.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Author: vvshuai
 * @Description: 有效期类型枚举
 * @Date: Created in 15:47 2020/9/4
 * @Modified By:
 */
@Getter
@AllArgsConstructor
public enum PeriodType {

    /**
     * @Description: 固定的
     * @return:
     */
    REGULAR("固定的(固定日期)",1),
    /**
     * @Description: 变动的
     * @return:
     */
    SHIFT("变动的(以领取之日开始计算)", 2);

    /** 有效期描述*/
    private String description;

    /** 有效期编码*/
    private Integer code;

    public static PeriodType of(Integer code){
        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(()-> new IllegalArgumentException(code + " not exist"));
    }
}
