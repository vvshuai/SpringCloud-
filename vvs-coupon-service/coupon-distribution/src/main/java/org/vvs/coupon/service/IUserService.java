package org.vvs.coupon.service;

import org.vvs.coupon.entity.Coupon;
import org.vvs.coupon.exception.CouponException;
import org.vvs.coupon.vo.AcquireTemplateRequest;
import org.vvs.coupon.vo.CouponTemplateSDK;

import java.util.List;

/**
 * @Author: vvshuai
 * @Description: 用户服务相关定义
 * @Date: Created in 22:47 2020/12/2
 * @Modified By:
 */
public interface IUserService {

    /**
     * @Description: 根据用户id 和状态查询优惠券状态
     * @return: java.util.List<org.vvs.coupon.entity.Coupon>
     */
    List<Coupon> findCouponByStatus(Long userId, Integer status) throws CouponException;

    /**
     * @Description: 和coupon-template 服务配合使用
     * @return:
     */
    List<CouponTemplateSDK> findAvailableTemplate(Long userId) throws CouponException;

    /**
     * @Description: 用户领取优惠券
     * @return: org.vvs.coupon.entity.Coupon
     */
    Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException;
}
