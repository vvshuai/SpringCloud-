package org.vvs.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: vvshuai
 * @Description: 获取优惠券请求对象定义
 * @Date: Created in 22:52 2020/12/2
 * @Modified By:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcquireTemplateRequest {

    /** 用户id */
    private Long userId;

    /** 优惠券模板信息 */
    private CouponTemplateSDK templateSDK;


}
