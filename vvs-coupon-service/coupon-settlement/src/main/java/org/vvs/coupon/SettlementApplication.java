package org.vvs.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Author: vvshuai
 * @Description: 优惠券结算微服务启动入口
 * @Date: Created in 21:33 2020/12/5
 * @Modified By:
 */
@SpringBootApplication
@EnableEurekaClient
public class SettlementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SettlementApplication.class, args);
    }
}
