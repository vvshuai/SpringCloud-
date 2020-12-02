package org.vvs.coupon.converter;

import org.vvs.coupon.constant.DistributeTarget;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @Author: vvshuai
 * @Description: 分发目标枚举转换器
 * @Date: Created in 23:27 2020/11/11
 * @Modified By:
 */
@Converter
public class DistributeTargetConverter
    implements AttributeConverter<DistributeTarget, Integer> {


    @Override
    public Integer convertToDatabaseColumn(DistributeTarget distributeTarget) {
        return distributeTarget.getCode();
    }

    @Override
    public DistributeTarget convertToEntityAttribute(Integer integer) {
        return DistributeTarget.of(integer);
    }
}
