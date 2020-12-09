package org.vvs.coupon.executor.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.vvs.coupon.constant.CouponCategory;
import org.vvs.coupon.constant.RuleFlag;
import org.vvs.coupon.executor.AbstractExecutor;
import org.vvs.coupon.executor.RuleExecutor;
import org.vvs.coupon.vo.GoodsInfo;
import org.vvs.coupon.vo.SettlementInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: vvshuai
 * @Description: 满减加折扣
 * @Date: Created in 23:09 2020/12/5
 * @Modified By:
 */
@Slf4j
@Component
public class ManJianZheKouExecutor extends AbstractExecutor implements RuleExecutor {

    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.MANJIAN_ZHEKOU;
    }

    /**
     * @Description: 用户传递的计算信息
     * @return: boolean
     */
    @Override
    protected boolean isGoodsTypeSatisfy(SettlementInfo settlement) {

        log.debug("Check ManJian And ZheKou IS Match Or Not!");
        List<Integer> goodsType = settlement.getGoodsInfos().stream()
                .map(GoodsInfo::getType).collect(Collectors.toList());

        List<Integer> templateGoodsType = new ArrayList<>();

        settlement.getCouponAndTemplateInfoList()
                .forEach(ct -> {
                    templateGoodsType.addAll(JSON.parseObject(
                            ct.getTemplateSDK().getRule().getUsage().getGoodsType(),
                            List.class
                    ));
                });

        // 如果想要使用多类优惠券 所有的商品类型都包含在内 差集为空
        return CollectionUtils.isEmpty(CollectionUtils.subtract(
                goodsType, templateGoodsType
        ));
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {

        double goodsSum = retain2Decimals(
                goodsCostSum(settlement.getGoodsInfos())
        );

        // 商品类型的校验
        SettlementInfo probability = processGoodsTypeNotSatisfy(
                settlement, goodsSum
        );
        if(null != probability) {
            log.debug("ManJian And ZheKou Template Is Not Match To GoodsType!");
            return probability;
        }

        SettlementInfo.CouponAndTemplateInfo manJian = null;
        SettlementInfo.CouponAndTemplateInfo zheKou = null;

        for (SettlementInfo.CouponAndTemplateInfo ct :
                settlement.getCouponAndTemplateInfoList()) {
            if (CouponCategory.of(ct.getTemplateSDK().getCategory()) ==
                    CouponCategory.MANJIAN) {
                manJian = ct;
            } else {
                zheKou = ct;
            }
        }

        assert null != manJian;
        assert null != zheKou;

        // 当前的优惠券和满减券 如果不能共用 清空优惠券 返回原价
        if(!isTemplateCanShared(manJian, zheKou)) {
            log.debug("Current ManJian And ZheKou Can Not Shared!");
            settlement.setCost(goodsSum);
            settlement.setCouponAndTemplateInfoList(Collections.emptyList());
            return settlement;
        }

        List<SettlementInfo.CouponAndTemplateInfo> ctInfos = new ArrayList<>();
        double manJianBase = (double) manJian.getTemplateSDK()
                .getRule().getDiscount().getBase();
        double manJianQuota = (double) manJian.getTemplateSDK()
                .getRule().getDiscount().getQuota();
        // 最终价格
        double targetSum = goodsSum;
        if(targetSum >= manJianBase) {
            targetSum -= manJianQuota;
            ctInfos.add(manJian);
        }

        // 计算折扣
        double zheKouQuota = (double) zheKou.getTemplateSDK().getRule()
                .getDiscount().getQuota();
        targetSum *= zheKouQuota * 1.0 / 100;
        ctInfos.add(zheKou);

        settlement.setCouponAndTemplateInfoList(ctInfos);
        settlement.setCost(retain2Decimals(
                Math.max(targetSum , doubleMinCost())
        ));

        log.debug("Use ManJian And ZheKou Coupon Make Goods Cost From {} To {}",
                goodsSum, settlement.getCost());

        return settlement;
    }

    /**
     * @Description: 去校验当前两张优惠券是否可以共用
     * 即校验 TemplateRule 中的 weight 是否满足条件
     * @return: boolean
     */
    private boolean
    isTemplateCanShared(SettlementInfo.CouponAndTemplateInfo manJian,
                        SettlementInfo.CouponAndTemplateInfo zheKou) {
        String manjianKey = manJian.getTemplateSDK().getKey()
                + String.format("%04d", manJian.getTemplateSDK().getId());
        String zhekouKey = zheKou.getTemplateSDK().getKey()
                + String.format("%04d", zheKou.getTemplateSDK().getId());
        List<String> allSharedKeysForManJian = new ArrayList<>();
        allSharedKeysForManJian.add(manjianKey);
        allSharedKeysForManJian.addAll(JSON.parseObject(
                manJian.getTemplateSDK().getRule().getWeight(),
                List.class
        ));

        List<String> allSharedKeysForZhekou = new ArrayList<>();
        allSharedKeysForZhekou.add(zhekouKey);
        allSharedKeysForZhekou.addAll(JSON.parseObject(
                zheKou.getTemplateSDK().getRule().getWeight(),
                List.class
        ));

        // 是否属于一个子集
        return CollectionUtils.isSubCollection(
                Arrays.asList(manjianKey, zhekouKey),
                allSharedKeysForManJian
        ) || CollectionUtils.isSubCollection(
                Arrays.asList(manjianKey, zhekouKey),
                allSharedKeysForZhekou
        );
    }
}
