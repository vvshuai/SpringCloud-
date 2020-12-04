package org.vvs.coupon.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vvs.coupon.exception.CouponException;
import org.vvs.coupon.vo.CommonResponse;
import org.vvs.coupon.vo.SettlementInfo;

/**
 * @Author: vvshuai
 * @Description: 结算微服务熔断策略实现
 * @Date: Created in 22:50 2020/12/4
 * @Modified By:
 */
@Slf4j
@Component
public class SettlementClientHystrix implements SettlementClient{

    /**
     * @Description: 优惠券规则计算
     * @return: org.vvs.coupon.vo.CommonResponse<org.vvs.coupon.vo.SettlementInfo>
     */
    @Override
    public CommonResponse<SettlementInfo> computeRule(SettlementInfo settlement) throws CouponException {

        log.error("[eureka-client-coupon-settlement] computeRule" + "request error");

        settlement.setEmploy(false);
        settlement.setCost(-1.0);


        return new CommonResponse<>(
                -1,
                "[eureka-client-coupon-settlement] request error",
                settlement
        );
    }

}
