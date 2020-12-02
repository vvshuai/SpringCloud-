package org.vvs.coupon.vo;

import org.vvs.coupon.constant.PeriodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: vvshuai
 * @Description: 优惠券规则定义
 * @Date: Created in 21:50 2020/10/15
 * @Modified By:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateRule {

    /** 优惠券过期规则 */
    private Expiration expiration;

    /** 折扣 */
    private Discount discount;

    /** 每个人最多领取多少张 */
    private Integer limitation;

    /** 使用地域 商品类型 */
    private Usage usage;

    /** 权重(可以和哪些优惠券叠加使用) 同一类无法叠加 优惠券唯一编码*/
    private String weight;

    /**
     * @Description: 校验功能
     * @return: boolean
     */
    public boolean validate() {


        return expiration.validate() && discount.validate()
                && limitation > 0 && usage.validate()
                && StringUtils.isNotEmpty(weight);
    }

    /**
     * @Description: 有效期限规则
     * @return:
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Expiration {

        /**有效期规则 对应code字段**/
        private Integer period;

        /** 有效间隔 只对变动性有效期有效*/
        private Integer gap;

        /** 优惠券模板失效日期 */
        private Long deadLine;

        boolean validate(){
            //最简单化校验
            return null != PeriodType.of(period) && gap > 0 && deadLine > 0;
        }

    }

    /**
     * @Description: 折扣 需要与类型配合决定
     * @return:
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Discount {

        /** 额度 满减(20) 折扣(85) 立减类型（10）*/
        private Integer quota;

        /** 基准值 需要满多少才可以用*/
        private Integer base;

        boolean validate() {

            return quota > 0 && base > 0;
        }
    }

    /**
     * @Description: 使用范围
     * @return:
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Usage {

        /** 省份 */
        private String province;

        /** 城市 */
        private String city;

        /** 商品类型 list->json->[文娱， 生鲜，家居，全品类]*/
        private String goodsType;

        boolean validate() {
            return StringUtils.isNotEmpty(province)
                    && StringUtils.isNotEmpty(city)
                    && StringUtils.isNotEmpty(goodsType);
        }
    }
}
