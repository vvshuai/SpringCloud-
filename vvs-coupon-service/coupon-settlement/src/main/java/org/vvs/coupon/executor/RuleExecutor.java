package org.vvs.coupon.executor;

import org.vvs.coupon.constant.RuleFlag;
import org.vvs.coupon.vo.SettlementInfo;

/**
 * @Author: vvshuai
 * @Description: 优惠券模板规则处理器接口定义
 * @Date: Created in 21:41 2020/12/5
 * @Modified By:
 */
public interface RuleExecutor {

    /**
     * @Description: 规则类型标记
     * @return: org.vvs.coupon.constant.RuleFlag
     */
    RuleFlag ruleConfig();

    /**
     * @Description: 优惠券规则的计算
     * @return: org.vvs.coupon.vo.SettlementInfo
     */
    SettlementInfo computeRule(SettlementInfo settlement);

    
}
