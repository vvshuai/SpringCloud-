package org.vvs.coupon.service;

import com.vvs.coupon.exception.CouponException;
import org.vvs.coupon.entity.CouponTemplate;
import org.vvs.coupon.vo.TemplateRequest;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 23:37 2020/11/29
 * @Modified By:
 */
public interface IBuildTemplateService {

    /**
     * @Description: 创建优惠券模板
     * @param: 模板信息请求对象
     * @return: org.vvs.coupon.entity.CouponTemplate
     */
    CouponTemplate buildTemplate(TemplateRequest request)
            throws CouponException;
}
