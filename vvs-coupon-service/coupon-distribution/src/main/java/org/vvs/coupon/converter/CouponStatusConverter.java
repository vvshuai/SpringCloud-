package org.vvs.coupon.converter;

import org.vvs.coupon.constant.CouponStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @Author: vvshuai
 * @Description: 优惠券状态枚举属性转换器
 * @Date: Created in 22:09 2020/12/2
 * @Modified By:
 */
@Converter
public class CouponStatusConverter
        implements AttributeConverter<CouponStatus, Integer> {


    @Override
    public Integer convertToDatabaseColumn(CouponStatus couponStatus) {
        return couponStatus.getCode();
    }

    @Override
    public CouponStatus convertToEntityAttribute(Integer code) {
        return CouponStatus.of(code);
    }
}
