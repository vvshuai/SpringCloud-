package org.vvs.coupon.service;

import com.vvs.coupon.exception.CouponException;
import com.vvs.coupon.vo.CouponTemplateSDK;
import org.vvs.coupon.entity.CouponTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author: vvshuai
 * @Description: 优惠券模板基础服务定义
 * @Date: Created in 15:18 2020/11/30
 * @Modified By:
 */
public interface ITemplateBaseService {

    /**
     * @Description: 根据id 获取优惠券模板信息
     * @return: org.vvs.coupon.entity.CouponTemplate
     */
    CouponTemplate buildTemplateInfo(Integer id) throws CouponException;

    /**
     * @Description: 查找可用的优惠券模板
     * @return: java.util.List<com.vvs.coupon.vo.CouponTemplateSDK>
     */
    List<CouponTemplateSDK> findAllUsalbeTemplate();

    /**
     * @Description: 获取映射
     * @return: java.util.Map<java.lang.Integer,com.vvs.coupon.vo.CouponTemplateSDK>
     */
    Map<Integer, CouponTemplateSDK> findIds2TemplateSDK(Collection<Integer> ids);
}
