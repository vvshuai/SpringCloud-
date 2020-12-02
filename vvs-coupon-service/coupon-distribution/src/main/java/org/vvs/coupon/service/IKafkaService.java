package org.vvs.coupon.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @Author: vvshuai
 * @Description: kafka相关接口
 * @Date: Created in 22:40 2020/12/2
 * @Modified By:
 */
public interface IKafkaService {

    /**
     * @Description: 消费kafka消息
     * @return: void
     */
    void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record);


}
