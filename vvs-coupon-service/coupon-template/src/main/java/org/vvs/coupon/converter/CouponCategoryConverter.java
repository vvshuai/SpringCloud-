package org.vvs.coupon.converter;

import com.vvs.coupon.constant.CouponCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @Author: vvshuai
 * @Description: 优惠券分类枚举转换器
 * @Date: Created in 23:47 2020/10/17
 * @Modified By: X是实体属性类型，
 */
@Converter
public class CouponCategoryConverter implements AttributeConverter<CouponCategory, String> {


    /**
     * @Description: 将当前的属性->X 转换到 Y 存储到数据库中
     *  update && insert
     * @return: java.lang.String
     */
    @Override
    public String convertToDatabaseColumn(CouponCategory couponCategory) {
        return couponCategory.getCode();
    }

    /**
     * @Description: 将数据库的字段Y 转换为实体属性 X， 查询操作时执行的操作
     * @return: com.vvs.coupon.constant.CouponCategory
     */
    @Override
    public CouponCategory convertToEntityAttribute(String code) {
        return CouponCategory.of(code);
    }
}
