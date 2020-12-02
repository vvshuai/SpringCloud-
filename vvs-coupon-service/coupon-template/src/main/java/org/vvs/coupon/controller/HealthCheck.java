package org.vvs.coupon.controller;

import org.vvs.coupon.exception.CouponException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: vvshuai
 * @Description: 健康检查接口
 * @Date: Created in 11:42 2020/12/1
 * @Modified By:
 */
@Slf4j
@RestController
public class HealthCheck {

    /** 服务发现客户端 */
    @Autowired
    private DiscoveryClient client;

    /** 服务注册接口 提供获取服务id的方法 */
    @Autowired
    private Registration registration;

    /**
     * @Description: 健康检查接口
     * @return: java.lang.String
     */
    @GetMapping("/health")
    public String health() {
        log.debug("view health api");
        return "CouponTemplate Is OK!";
    }

    /**
     * @Description: 异常测试接口
     * @return: java.lang.String
     */
    @GetMapping("/exception")
    public String exception() throws CouponException {

        log.debug("view exception api");

        throw new CouponException("CouponTemplate Has Some Problem");
    }

    /**
     * @Description: 获取 Eureka Server 上的微服务原信息
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    @GetMapping("/info")
    public List<Map<String, Object>> info() {

        // 等待两分钟 才能获取注册信息
        List<ServiceInstance> instances
                = client.getInstances(registration.getServiceId());

        List<Map<String, Object>> result = new ArrayList<>(instances.size());

        instances.forEach(i -> {

            Map<String, Object> info = new HashMap<>();

            info.put("serviceId", i.getServiceId());
            info.put("instanceId", i.getInstanceId());
            info.put("port", i.getPort());

            result.add(info);
        });

        return result;
    }
}
