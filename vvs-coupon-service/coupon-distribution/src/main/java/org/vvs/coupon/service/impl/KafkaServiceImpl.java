package org.vvs.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.vvs.coupon.constant.Constant;
import org.vvs.coupon.constant.CouponStatus;
import org.vvs.coupon.dao.CouponDao;
import org.vvs.coupon.entity.Coupon;
import org.vvs.coupon.service.IKafkaService;
import org.vvs.coupon.vo.CouponKafkaMessage;

import java.util.List;
import java.util.Optional;

/**
 * @Author: vvshuai
 * @Description: kafka 服务相关接口实现 异步化处理
 * 核心思想 将cache中的coupon的状态变化同步到 DB 中
 * @Date: Created in 22:07 2020/12/3
 * @Modified By:
 */
@Slf4j
@Component
public class KafkaServiceImpl implements IKafkaService {

    @Autowired
    private CouponDao couponDao;

    /**
     * @Description: 消费kafka 消息
     * @return: void
     */
    @Override
    @KafkaListener(topics = {Constant.TOPIC}, groupId = "vvs-coupon-1")
    public void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            CouponKafkaMessage couponInfo = JSON.parseObject(
                    message.toString(),
                    CouponKafkaMessage.class
            );

            log.info("Receive CouponKafkaMessage: {}", message.toString());

            CouponStatus status = CouponStatus.of(couponInfo.getStatus());

            switch (status) {
                case USABLE:
                    break;
                case USED:
                    processUsedCoupons(couponInfo, status);
                    break;
                case EXPIRED:
                    processExpiredCoupons(couponInfo, status);
                    break;
            }
        }
    }

    /**
     * @Description: 根据状态处理优惠券信息
     * @return: void
     */
    private void processCouponsByStatus(CouponKafkaMessage message, CouponStatus status) {

        List<Coupon> coupons = couponDao.findAllById(
                message.getIds()
        );
        if(CollectionUtils.isEmpty(coupons)
                || coupons.size() != message.getIds().size()) {

            log.error("Can Not Find Right Coupon Info: {}", JSON.toJSONString(message));

            // TODO 发送邮件
            return ;
        }

        coupons.forEach(c -> c.setStatus(status));
        log.info("CouponKafkaMessage Op Coupon Count: {}",
                couponDao.saveAll(coupons).size());

    }

    /**
     * @Description: 处理已经使用的用户优惠券
     * @return: void
     */
    private void processUsedCoupons(CouponKafkaMessage message, CouponStatus status) {
        // TODO 给用户发送短信
        processCouponsByStatus(message, status);
    }

    /**
     * @Description: 处理过期的用户优惠券
     * @return: void
     */
    private void processExpiredCoupons(CouponKafkaMessage message, CouponStatus status) {
        // TODO 给用户发送推送
        processCouponsByStatus(message, status);
    }
}
