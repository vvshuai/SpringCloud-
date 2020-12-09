package org.vvs.coupon.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.vvs.coupon.constant.CouponCategory;
import org.vvs.coupon.constant.RuleFlag;
import org.vvs.coupon.exception.CouponException;
import org.vvs.coupon.vo.SettlementInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: vvshuai
 * @Description: 优惠券结算规则执行管理器
 * 即根据用户的请求（SettlementInfo)找到对应的Executor 做结算
 * BeanPostProcessor: Bean 后置处理器
 * @Date: Created in 20:39 2020/12/8
 * @Modified By:
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class ExecuteManager implements BeanPostProcessor {

    /**
     * @Description: 规则执行器映射
     * @return:
     */
    private static Map<RuleFlag, RuleExecutor> executorIndex =
            new HashMap<>(RuleFlag.values().length);

    /**
     * @Description: 优惠券结算规则入口
     * @return: org.vvs.coupon.vo.SettlementInfo
     */
    public SettlementInfo computeRule(SettlementInfo settlement)
            throws CouponException {

        SettlementInfo result = null;

        // 单类优惠券
        if(settlement.getCouponAndTemplateInfoList().size() == 1) {

            // 获取优惠券类别
            CouponCategory couponCategory = CouponCategory.of(
                    settlement.getCouponAndTemplateInfoList().get(0).getTemplateSDK()
                    .getCategory()
            );
            switch (couponCategory) {
                case LIJIAN:
                    result = executorIndex.get(RuleFlag.LIJIAN)
                            .computeRule(settlement);
                    break;
                case ZHEKOU:
                    result = executorIndex.get(RuleFlag.ZHEKOU)
                            .computeRule(settlement);
                    break;
                case MANJIAN:
                    result = executorIndex.get(RuleFlag.MANJIAN)
                            .computeRule(settlement);
                    break;
            }
        } else {

            // 多类优惠券
            List<CouponCategory> categories = new ArrayList<>(
                    settlement.getCouponAndTemplateInfoList().size()
            );

            settlement.getCouponAndTemplateInfoList().forEach(
                    ct -> categories.add(CouponCategory.of(ct.getTemplateSDK().getCategory()))
            );
            if (categories.size() != 2) {
                throw new CouponException("Not Support More Template Category");
            } else {
                if (categories.contains(CouponCategory.MANJIAN)
                && categories.contains(CouponCategory.ZHEKOU)) {
                     result = executorIndex.get(RuleFlag.MANJIAN_ZHEKOU)
                             .computeRule(settlement);
                } else {
                    throw new CouponException("Not Support For Other Template Category");
                }
            }
        }
        return result;
    }
    /**
     * @Description: 在bean初始化前去执行
     * @return: java.lang.Object
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {

        if (!(bean instanceof RuleExecutor)) {
            return bean;
        }

        RuleExecutor executor = (RuleExecutor) bean;
        RuleFlag ruleFlag = executor.ruleConfig();

        // 重复注册
        if (executorIndex.containsKey(ruleFlag)) {
            throw new IllegalStateException("There is already an executor " +
                    "for rule flag: " + ruleFlag);
        }

        log.info("Load executor {} for rule flag {}.",
                executor.getClass(), ruleFlag);
        executorIndex.put(ruleFlag, executor);
        return null;
    }

    /**
     * @Description: 初始化之后去执行
     * @return: java.lang.Object
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {

        return null;
    }
}
