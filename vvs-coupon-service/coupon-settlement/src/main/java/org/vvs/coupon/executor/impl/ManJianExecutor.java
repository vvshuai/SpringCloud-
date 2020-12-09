package org.vvs.coupon.executor.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vvs.coupon.constant.RuleFlag;
import org.vvs.coupon.executor.AbstractExecutor;
import org.vvs.coupon.executor.RuleExecutor;
import org.vvs.coupon.vo.CouponTemplateSDK;
import org.vvs.coupon.vo.SettlementInfo;

import java.util.Collections;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 22:11 2020/12/5
 * @Modified By:
 */
@Slf4j
@Component
public class ManJianExecutor extends AbstractExecutor implements RuleExecutor {

    @Override
    public RuleFlag ruleConfig() {

        return RuleFlag.MANJIAN;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {

        double goodsSum = retain2Decimals(
                goodsCostSum(settlement.getGoodsInfos())
        );
        SettlementInfo probability = processGoodsTypeNotSatisfy(
                settlement, goodsSum
        );

        if(null != probability) {
            log.debug("ManJian Template Is Not Match To GoodsType!");
            return probability;
        }

        // 判断满减是否符合折扣标准
        CouponTemplateSDK templateSDK = settlement.getCouponAndTemplateInfoList()
                .get(0).getTemplateSDK();

        double base = (double) templateSDK.getRule().getDiscount().getBase();
        double quota = (double) templateSDK.getRule().getDiscount().getQuota();

        // 如果不符合标准 则直接返回商品总价
        if (goodsSum < base) {
            log.debug("Current Goods Cost Sum < ManJian Coupon Base!");
            settlement.setCost(goodsSum);
            settlement.setCouponAndTemplateInfoList(Collections.emptyList());
            return settlement;
        }

        // 符合标准 计算使用优惠券后的价格
        settlement.setCost(retain2Decimals(
                (goodsSum - quota) > doubleMinCost() ? (goodsSum - quota) : doubleMinCost()
        ));
        log.debug("Use Manjian Coupon Make Goods Cost From {} To {}", goodsSum, settlement.getCost());

        return settlement;
    }
}
