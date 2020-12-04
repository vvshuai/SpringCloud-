package org.vvs.coupon.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.vvs.coupon.feign.hystrix.TemplateClientHystrix;
import org.vvs.coupon.vo.CommonResponse;
import org.vvs.coupon.vo.CouponTemplateSDK;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Author: vvshuai
 * @Description: 优惠券模板微服务 feign 接口定义
 * @Date: Created in 22:15 2020/12/4
 * @Modified By:
 */
@FeignClient(value = "eureka-client-coupon-template",
        fallback = TemplateClientHystrix.class)
public interface TemplateClient {

    /**
     * @Description: 查找所有可用的优惠券模板
     * @return:
     */
    @RequestMapping(value = "/coupon-template/template/sdk/all",
            method = RequestMethod.GET)
    CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplate();

    /**
     * @Description: 获取 ids 到 CouponTemplateSDK 的映射
     * @return:
     */
    @RequestMapping(value = "/coupon-template/template/sdk/infos",
            method = RequestMethod.GET)
    CommonResponse<Map<Integer, CouponTemplateSDK>> findIds2TemplateSDK(
            @RequestParam("ids") Collection<Integer> ids
    );


}
