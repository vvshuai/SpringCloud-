package org.vvs.coupon.service;

import org.vvs.coupon.entity.Coupon;
import org.vvs.coupon.exception.CouponException;

import java.util.List;

/**
 * @Author: vvshuai
 * @Description: redis 相关操作接口定义
 *  用户的三个状态优惠券 cache 相关操作
 *  优惠券模板生成的优惠券码 cache 操作
 *
 * @Date: Created in 22:21 2020/12/2
 * @Modified By:
 */
public interface IRedisService {

    /**
     * @Description: 根据userId 和状态 找到缓存优惠券列表
     * @return: java.util.List<org.vvs.coupon.entity.Coupon>
     */
    List<Coupon> getCachedCoupons(Long userId, Integer status);

    /**
     * @Description: 保存空的优惠券列表
     * @return: void
     */
    void saveEmptyCouponListToCache(Long userId, List<Integer> status);

    /**
     * @Description: 从cache中获取一个优惠券码
     * @return: java.lang.String
     */
    String tryToAcquireCouponCodeFromCache(Integer templateId);

    /**
     * @Description: 将优惠券加入缓存
     * @return: java.lang.Integer
     */
    Integer addCouponToCache(Long userId, List<Coupon> coupons, Integer status)
            throws CouponException;
}
