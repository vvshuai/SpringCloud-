package org.vvs.coupon.vo;

import org.vvs.coupon.constant.CouponCategory;
import org.vvs.coupon.constant.DistributeTarget;
import org.vvs.coupon.constant.ProductLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

/**
 * @Author: vvshuai
 * @Description: 优惠券模板创建请求对象
 * @Date: Created in 23:06 2020/11/29
 * @Modified By:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRequest {

    /** 优惠券名称 */
    private String name;

    /** 优惠券logo */
    private String logo;

    /** 优惠券描述 */
    private String desc;

    /** 优惠券分类 */
    private String category;

    /** 产品线 */
    private Integer productLine;

    /** 总数 */
    private Integer count;

    /** 创建用户 */
    private Long userId;

    /** 目标用户 */
    private Integer target;

    /** 优惠券规则 */
    private TemplateRule rule;

    /**
     * @Description: 校验对象的合法性
     * @return: boolean
     */
    public boolean validate() {

        boolean stringValid = StringUtils.isNotEmpty(name)
                && StringUtils.isNotEmpty(logo)
                && StringUtils.isNotEmpty(desc);

        boolean enumValid = null != CouponCategory.of(category)
                && null != ProductLine.of(productLine)
                && null != DistributeTarget.of(target);

        boolean numValid = count > 0 && userId > 0;

        return stringValid && enumValid && numValid && rule.validate();
    }
}
