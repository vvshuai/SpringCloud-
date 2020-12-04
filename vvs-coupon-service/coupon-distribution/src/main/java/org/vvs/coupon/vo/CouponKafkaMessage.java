package org.vvs.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: vvshuai
 * @Description: 优惠券 kafka 消息对象定义
 * @Date: Created in 22:16 2020/12/3
 * @Modified By:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponKafkaMessage {

    /** 优惠券状态类型 */
    private Integer status;

    /** Coupon 主键 */
    private List<Integer> ids;
}
