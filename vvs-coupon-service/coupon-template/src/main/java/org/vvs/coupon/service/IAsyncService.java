package org.vvs.coupon.service;

import org.vvs.coupon.entity.CouponTemplate;

/**
 * @Author: vvshuai
 * @Description: 异步服务接口定义
 * @Date: Created in 15:06 2020/11/30
 * @Modified By:
 */
public interface IAsyncService {

    /**
     * @Description: <h2>根据模板异步创建优惠券码</h2>
     * @param couponTemplate {@link CouponTemplate} 优惠券模板
     * @return: void
     */
    void asyncConstructCouponByTemplate(CouponTemplate couponTemplate);


}
