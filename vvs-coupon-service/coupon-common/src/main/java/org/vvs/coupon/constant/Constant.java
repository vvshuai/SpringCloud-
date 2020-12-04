package org.vvs.coupon.constant;

/**
 * @Author: vvshuai
 * @Description: 常用常量定义
 * @Date: Created in 21:13 2020/11/30
 * @Modified By:
 */
public class Constant {

    /** kafka 消息的 topic */
    public static final String TOPIC = "vvs_user_coupon_op";

    /**
     * @Description: redis key 前缀定义
     * @return:
     */
    public static class RedisPrefix {

        /** 优惠券码 key 前缀*/
        public static final String COUPON_TEMPLATE = "vvs_coupon_template_code_";

        /** 用户当前所有可用的优惠券key 的前缀 */
        public static final String USER_COUPON_USABLE = "vvs_user_coupon_usable_";

        /** 用户当前所有已使用的优惠券key 前缀*/
        public static final String USER_COUPON_USED = "vvs_user_coupon_used_";

        /** 用户当前所有已经过期的优惠券 前缀*/
        public static final String USER_COUPON_EXPIRED = "vvs_user_coupon_user_expired_";

    }
}
