package org.vvs.coupon.feign.hystrix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.vvs.coupon.feign.TemplateClient;
import org.vvs.coupon.vo.CommonResponse;
import org.vvs.coupon.vo.CouponTemplateSDK;

import java.util.*;

/**
 * @Author: vvshuai
 * @Description: 优惠券模板 Feign 接口 熔断降级策略
 * @Date: Created in 22:35 2020/12/4
 * @Modified By:
 */
@Slf4j
@Component
public class TemplateClientHystrix implements TemplateClient {

    /**
     * @Description: 查找所有可用的优惠券模板
     * @return: org.vvs.coupon.vo.CommonResponse<java.util.List<org.vvs.coupon.vo.CouponTemplateSDK>>
     */
    @Override
    public CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplate() {

        log.error("[eureka-client-coupon-template] findAllUsableTemplate " +
                "request error");

        return new CommonResponse<>(
                -1,
                "[eureka-client-coupon-template] request error",
                Collections.emptyList()
        );
    }

    /**
     * @Description: 获取模板 ids 到 CouponTemplateSDK 的映射
     * @return: org.vvs.coupon.vo.CommonResponse<java.util.Map<java.lang.Integer,org.vvs.coupon.vo.CouponTemplateSDK>>
     */
    @Override
    public CommonResponse<Map<Integer, CouponTemplateSDK>>
    findIds2TemplateSDK(Collection<Integer> ids) {
        log.error("[eureka-client-coupon-template] findIds2TemplateSDK" + "request error");

        return new CommonResponse<>(
                -1,
                "[eureka-client-coupon-template] request error",
                new HashMap<>()
        );
    }
}
