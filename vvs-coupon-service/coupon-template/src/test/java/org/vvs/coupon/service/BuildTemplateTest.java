package org.vvs.coupon.service;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.vvs.coupon.constant.CouponCategory;
import org.vvs.coupon.constant.DistributeTarget;
import org.vvs.coupon.constant.PeriodType;
import org.vvs.coupon.constant.ProductLine;
import org.vvs.coupon.vo.TemplateRequest;
import org.vvs.coupon.vo.TemplateRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * @Author: vvshuai
 * @Description: 构造优惠模板服务测试
 * @Date: Created in 15:23 2020/12/2
 * @Modified By:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BuildTemplateTest {

    @Autowired
    private  IBuildTemplateService buildTemplateService;

    @Test
    public void testBuildTemplate() throws Exception {

        System.out.println(JSON.toJSONString(
                buildTemplateService.buildTemplate(fakeTemplateRequest())
        ));

        Thread.sleep(5000);
    }

    /**
     * @Description: 生成一个templateRequest代码
     * @return:
     */
    private TemplateRequest fakeTemplateRequest() {

        TemplateRequest request = new TemplateRequest();
        request.setName("优惠券模板-" + new Date().getTime());
        request.setLogo("http://www.imooc.com");
        request.setDesc("这是一张优惠券模板");
        request.setCategory(CouponCategory.LIJIAN.getCode());
        request.setProductLine(ProductLine.DABAO.getCode());
        request.setCount(10);
        request.setUserId(10001L);
        request.setTarget(DistributeTarget.SINGLE.getCode());

        TemplateRule rule = new TemplateRule();
        rule.setExpiration(new TemplateRule.Expiration(
                PeriodType.SHIFT.getCode(),
                1, DateUtils.addDays(new Date(), 60).getTime()
        ));
        rule.setDiscount(new TemplateRule.Discount(5, 1));
        rule.setLimitation(1);
        rule.setUsage(new TemplateRule.Usage(
                "辽宁省", "朝阳市",
                JSON.toJSONString(Arrays.asList("家居"))
        ));
        rule.setWeight(JSON.toJSONString(Collections.EMPTY_LIST));

        request.setRule(rule);

        return request;
    }
}
