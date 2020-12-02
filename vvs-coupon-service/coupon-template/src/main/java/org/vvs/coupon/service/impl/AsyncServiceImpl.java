package org.vvs.coupon.service.impl;

import com.google.common.base.Stopwatch;
import org.vvs.coupon.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.vvs.coupon.dao.CouponTemplateDao;
import org.vvs.coupon.entity.CouponTemplate;
import org.vvs.coupon.service.IAsyncService;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: vvshuai
 * @Description: 异步服务接口实现
 * @Date: Created in 21:20 2020/11/30
 * @Modified By:
 */

@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {

    @Autowired
    private CouponTemplateDao templateDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Async("getAsyncExecutor")
    @Override
    @SuppressWarnings("all")
    public void asyncConstructCouponByTemplate(CouponTemplate couponTemplate) {

        Stopwatch stopwatch = Stopwatch.createStarted();

        Set<String> couponCodes = buildCouponCode(couponTemplate);

        // vvs_coupon_template_code_1
        String redisKey = String.format("%s%s",
                Constant.RedisPrefix.COUPON_TEMPLATE, couponTemplate.getId().toString());
        log.info("Push CouponCode to Redis: {}",
                redisTemplate.opsForList().rightPushAll(redisKey, couponCodes));
        couponTemplate.setAvailable(true);
        templateDao.save(couponTemplate);

        stopwatch.stop();
        log.info("Construct CouponCode By Template Cost: {}ms",
                stopwatch.elapsed(TimeUnit.MILLISECONDS));

        // TODO 发送短信或者邮件通知优惠券模板已经可用
        log.info("CouponTemplate({}) Is Available!", couponTemplate.getId());

    }

    /**
     * @Description: 构造优惠券码
     * 优惠券码 对于每一张优惠券 18位
     * 前四位 产品线 + 类型
     * 中间六位 日期随机（190101）
     * 后八位 0~9随机数构成
     * @param template {@link CouponTemplate} 实体类
     * @return: java.util.Set<java.lang.String>
     */
    @SuppressWarnings("all")
    private Set<String> buildCouponCode(CouponTemplate template) {

        Stopwatch stopwatch = Stopwatch.createStarted();

        Set<String> result = new HashSet<>(template.getCount());

        // 前四位的生成
        String prefix4 = template.getProductLine().getCode().toString()
                + template.getCategory().getCode();

        String date = new SimpleDateFormat("yyMMdd").format(template.getCreateTime());

        for (int i = 0;i != template.getCount(); i++) {
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }

        while(result.size() < template.getCount()) {
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }

        assert result.size() == template.getCount();

        stopwatch.stop();
        log.info("Build Coupon Code Cost: {}ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));

        return result;
    }

    /**
     * @Description: 构造优惠券码的后14位
     * @param date 创建优惠券的日期
     * @return:
     */
    private String buildCouponCodeSuffix14(String date) {

        char[] bases = new char[]{'1','2','3','4','5','6','7','8','9'};

        // 中间六位
        List<Character> chars = date.chars()
                .mapToObj(e -> (char) e).collect(Collectors.toList());

        Collections.shuffle(chars);
        String mid6 = chars.stream()
                .map(Objects::toString).collect(Collectors.joining());

        // 后八位
        String suffix8 = RandomStringUtils.random(1, bases)
                + RandomStringUtils.randomNumeric(7);

        return mid6 + suffix8;
    }
}
