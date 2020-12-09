package org.vvs.coupon.executor;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.CollectionUtils;
import org.vvs.coupon.vo.GoodsInfo;
import org.vvs.coupon.vo.SettlementInfo;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: vvshuai
 * @Description: 规则执行器抽象类 定义通用方法
 * @Date: Created in 21:45 2020/12/5
 * @Modified By:
 */
public abstract class AbstractExecutor {

    /**
     * @Description: 校验商品类型与优惠券是否匹配
     * 需要注意：
     * 这里实现的是单品类优惠券的校验，多品类优惠券重载此方法
     * 商品只需要有一个优惠券要求的商品类型去匹配就可以
     * @return: boolean
     */
    @SuppressWarnings("all")
    protected boolean isGoodsTypeSatisfy(SettlementInfo settlement) {

        List<Integer> goodsType = settlement.getGoodsInfos()
                .stream().map(GoodsInfo::getType)
                .collect(Collectors.toList());

        List<Integer> templateGoodsType = JSON.parseObject(
                settlement.getCouponAndTemplateInfoList().get(0).getTemplateSDK()
                        .getRule().getUsage().getGoodsType(),
                List.class
        );

        // 存在交集即可
        return CollectionUtils.isNotEmpty(
                CollectionUtils.intersection(goodsType, templateGoodsType)
        );
    }

    /**
     * @Description: 处理商品类型与优惠券限制不匹配的情况
     * @return: org.vvs.coupon.vo.SettlementInfo
     */
    protected SettlementInfo processGoodsTypeNotSatisfy(
        SettlementInfo settlement, double goodsSum
    ) {
        boolean isGoodsTypeSatisfy = isGoodsTypeSatisfy(settlement);

        // 当商品类型不满足时 返回总价 并清空优惠券
        if (!isGoodsTypeSatisfy) {
            settlement.setCost(goodsSum);
            settlement.setCouponAndTemplateInfoList(Collections.emptyList());
            return settlement;
        }

        return null;
    }

    /**
     * @Description: 商品总价
     * @return: double
     */
    protected double goodsCostSum(List<GoodsInfo> goodsInfos) {

        return goodsInfos.stream().mapToDouble(
                g -> g.getPrice() * g.getCount()
        ).sum();
    }

    /**
     * @Description: 保留两位小数
     * @return: double
     */
    protected double retain2Decimals(double value) {

        return new BigDecimal(value).setScale(
                2, BigDecimal.ROUND_CEILING
        ).doubleValue();
    }

    /**
     * @Description: 最小支付费用
     * @return:
     */
    protected double doubleMinCost() {

        return 0.1;
    }
}
