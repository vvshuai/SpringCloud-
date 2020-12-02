package org.vvs.coupon.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vvs.coupon.constant.CouponStatus;
import org.vvs.coupon.entity.Coupon;

import java.util.List;

/**
 * @Author: vvshuai
 * @Description: Coupon Dao 接口定义
 * @Date: Created in 22:17 2020/12/2
 * @Modified By:
 */

public interface CouponDao extends JpaRepository<Coupon, Integer> {

    /**
     * @Description: 根据 userId + 状态寻找优惠券记录
     * @return: java.util.List<org.vvs.coupon.entity.Coupon>
     */
    List<Coupon> findAllByUserIdAndStatus(Long userId, CouponStatus status);
}
