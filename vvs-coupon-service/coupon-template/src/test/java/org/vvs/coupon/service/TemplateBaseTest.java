package org.vvs.coupon.service;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.vvs.coupon.exception.CouponException;

import java.util.Arrays;

/**
 * @Author: vvshuai
 * @Description: 优惠券模板基础服务
 * @Date: Created in 20:32 2020/12/2
 * @Modified By:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TemplateBaseTest {

    @Autowired
    private ITemplateBaseService templateBaseService;

    @Test
    public void testBuildTemplateInfo() throws CouponException {

        System.out.println(JSON.toJSONString(templateBaseService.buildTemplateInfo(10)));
        System.out.println(JSON.toJSONString(templateBaseService.buildTemplateInfo(2)));
    }

    @Test
    public void testFindAllUsableTemplate() {

        System.out.println(JSON.toJSONString(
                templateBaseService.findAllUsalbeTemplate()
        ));
    }

    @Test
    public void testFindId2TemplateSDK() {

        System.out.println(JSON.toJSONString(
                templateBaseService.findIds2TemplateSDK(Arrays.asList(1, 2, 3))
        ));
    }
}
