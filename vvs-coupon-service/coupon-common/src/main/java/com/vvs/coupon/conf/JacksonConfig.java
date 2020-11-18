package com.vvs.coupon.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @Author: vvshuai
 * @Description: JackSon的自定义配置
 * @Date: Created in 17:39 2020/7/2
 * @Modified By:
 */
@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper getObjectMapper(){

        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return mapper;
    }
}
