package org.vvs.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 22:40 2020/7/5
 * @Modified By:
 */
@EnableEurekaClient
@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class TemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }
}
