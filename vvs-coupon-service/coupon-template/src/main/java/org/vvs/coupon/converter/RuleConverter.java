package org.vvs.coupon.converter;

import com.alibaba.fastjson.JSON;
import org.vvs.coupon.vo.TemplateRule;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 23:31 2020/11/11
 * @Modified By:
 */
@Converter
public class RuleConverter
        implements AttributeConverter<TemplateRule, String> {


    @Override
    public String convertToDatabaseColumn(TemplateRule rule) {
        return JSON.toJSONString(rule);
    }

    @Override
    public TemplateRule convertToEntityAttribute(String rule) {
        return JSON.parseObject(rule, TemplateRule.class);
    }
}
