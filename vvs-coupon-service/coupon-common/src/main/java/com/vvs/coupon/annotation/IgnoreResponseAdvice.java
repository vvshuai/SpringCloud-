package com.vvs.coupon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: vvshuai
 * @Description: 忽略统一响应注解定义
 * @Date: Created in 18:03 2020/7/2
 * @Modified By:
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResponseAdvice {


}
