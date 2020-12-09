package org.vvs.coupon.executor.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vvs.coupon.constant.RuleFlag;
import org.vvs.coupon.executor.AbstractExecutor;
import org.vvs.coupon.executor.RuleExecutor;
import org.vvs.coupon.vo.CouponTemplateSDK;
import org.vvs.coupon.vo.SettlementInfo;

/**
 * @Author: vvshuai
 * @Description: 折扣优惠券结算规则执行器
 * @Date: Created in 22:39 2020/12/5
 * @Modified By:
 */
@Slf4j
@Component
public class ZheKouExecutor extends AbstractExecutor implements RuleExecutor {

    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.ZHEKOU;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {

        double goodsSum = retain2Decimals(
                goodsCostSum(settlement.getGoodsInfos())
        );
        SettlementInfo probability = processGoodsTypeNotSatisfy(
                settlement, goodsSum
        );

        if (null  != probability) {
            log.debug("ZheKou Template Is Not Match GoodsType!");
            return probability;
        }

        CouponTemplateSDK templateSDK = settlement.getCouponAndTemplateInfoList()
                .get(0).getTemplateSDK();

        double quota = (double) templateSDK.getRule().getDiscount().getQuota();

        // 直接结算价格
        settlement.setCost(
                Math.max(retain2Decimals((goodsSum * (quota * 1.0 / 100))), doubleMinCost())
        );

        log.debug("Use ZheKou Coupon Make Goods Cost From {} To {}",
                goodsSum, settlement.getCost());

        return settlement;
    }
}
