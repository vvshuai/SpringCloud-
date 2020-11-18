package org.vvs.coupon.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vvs.coupon.entity.CouponTemplate;

import java.util.List;

/**
 * @Author: vvshuai
 * @Description: CouponTemplate Dao 接口
 * @Date: Created in 22:54 2020/11/17
 * @Modified By:
 */
public interface CouponTemplateDao
        extends JpaRepository<CouponTemplate, Integer> {

    /**
     * @Description: 根据模板名称查询模板
     * @return: org.vvs.coupon.entity.CouponTemplate
     */
    CouponTemplate findByName(String name);

    /**
     * @Description: 根据 available 和 expired 标记查找模板记录
     * @return: java.util.List<org.vvs.coupon.entity.CouponTemplate>
     */
    List<CouponTemplate> findAllByAvAndAvailableAndExpired(
        Boolean available, Boolean expired
    );

    /**
     * @Description: 根据 expired 标记查找模板记录
     * @return: java.util.List<org.vvs.coupon.entity.CouponTemplate>
     */
    List<CouponTemplate> findAllByExpired(Boolean expired);
}
