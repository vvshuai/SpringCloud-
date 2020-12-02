package org.vvs.coupon.schedule;

import org.vvs.coupon.vo.TemplateRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.vvs.coupon.dao.CouponTemplateDao;
import org.vvs.coupon.entity.CouponTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: vvshuai
 * @Description: 定时清理已过期的优惠券模板
 * @Date: Created in 23:57 2020/11/30
 * @Modified By:
 */
@Slf4j
@Component
public class ScheduledTask {

    @Autowired
    private CouponTemplateDao templateDao;

    /**
     * @Description: 下线已过期的优惠券
     * @return: void
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void offlineCouponTemplate() {

        log.info("Start To Expire CouponTemplate");

        List<CouponTemplate> templates = templateDao.findAllByExpired(false);

        if(CollectionUtils.isEmpty(templates)) {
            log.info("Done To Expired CouponTemplate.");
            return ;
        }

        Date cur = new Date();
        List<CouponTemplate> expiredTemplates = new ArrayList<>(templates.size());

        templates.forEach(t ->{

            // 根据优惠券模板规则中 过期规则
            TemplateRule rule = t.getRule();
            if (rule.getExpiration().getDeadLine() < cur.getTime()) {
                t.setExpired(true);
                expiredTemplates.add(t);
            }

        });

        if(CollectionUtils.isNotEmpty(expiredTemplates)) {
            log.info("Expired CouponTemplate Num : {}", templateDao.saveAll(expiredTemplates));
        }

        log.info("Done To Expired CouponTemplate.");
    }
}
