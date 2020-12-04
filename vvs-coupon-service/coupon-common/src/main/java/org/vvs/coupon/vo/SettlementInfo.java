package org.vvs.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: vvshuai
 * @Description: 结算信息对象定义
 * @Date: Created in 23:48 2020/12/2
 * @Modified By:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettlementInfo {

    /** 用户 Id */
    private Long userId;

    /** 商品信息 */
    private List<GoodsInfo> goodsInfos;

    /** 优惠券列表 */
    private List<CouponAndTemplateInfo> couponAndTemplateInfoList;

    /** 是否结算生效，及结算 */
    private Boolean employ;

    /** 结果结算金额 */
    private Double cost;

    /**
     *优惠券 模板信息
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CouponAndTemplateInfo {

        /** Coupon主键 */
        private Integer id;

        /** 优惠券对应模板信息 */
        private CouponTemplateSDK templateSDK;
    }
}
