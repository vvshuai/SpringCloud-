package org.vvs.coupon.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.vvs.coupon.exception.CouponException;
import org.vvs.coupon.vo.CommonResponse;
import org.vvs.coupon.vo.SettlementInfo;

/**
 * @Author: vvshuai
 * @Description: 优惠券规则结算微服务
 * @Date: Created in 22:28 2020/12/4
 * @Modified By:
 */
@FeignClient(value = "eureka-client-coupon-settlement",
            fallback = SettlementClientHystrix.class)
public interface SettlementClient {

    /**
     * @Description: 优惠券结算定义
     * @return: org.vvs.coupon.vo.CommonResponse<org.vvs.coupon.vo.SettlementInfo>
     */
    @RequestMapping(value = "/coupon-settlement/settlement/compute",
            method = RequestMethod.POST)
    CommonResponse<SettlementInfo> computeRule(
            @RequestBody SettlementInfo settlement) throws CouponException;


}
