package com.vvs.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: vvshuai
 * @Description: 微服务之间用的优惠券模板信息定义
 * @Date: Created in 15:12 2020/11/30
 * @Modified By:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponTemplateSDK {

    /** 优惠券模板主键*/
    private Integer id;

    /** 优惠券模板名称 */
    private String name;

    /** 优惠券logo */
    private String logo;

    /** 优惠券描述信息 */
    private String desc;

    /** 优惠券分类 */
    private String category;

    /** 产品线 */
    private Integer productLine;

    /** 优惠券模板的编码 */
    private String key;

    /** 目标用户 */
    private Integer target;

    /** 优惠券规则定义 */
    private TemplateRule rule;


}
