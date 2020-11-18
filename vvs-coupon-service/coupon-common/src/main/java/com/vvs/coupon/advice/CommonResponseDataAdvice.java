package com.vvs.coupon.advice;

import com.vvs.coupon.annotation.IgnoreResponseAdvice;
import com.vvs.coupon.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Author: vvshuai
 * @Description: 统一响应
 * @Date: Created in 18:06 2020/7/2
 * @Modified By:
 */
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    /**
     * @Description: 判断是否需要对响应进行处理
     * @return: boolean
     */
    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 如果当前方法所在类标识了 @IgnoreResponseAdvice 注解， 不需要处理
        if(methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)){
            return false;
        }

        // 如果当前方法标识了 @IgnoreResponseAdvice 注解，不需要处理
        if (methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)){
            return false;
        }
        // 对相应进行处理,执行 beforeBodyWrite 方法
        return true;
    }

    /**
     * @Description: 响应返回之前的处理
     * @return: java.lang.Object
     */
    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        //定义最终的返回对象
        CommonResponse<Object> response = new CommonResponse<>(
            0, ""
        );
        // 如果 o 是 null, response 不需要设置 data
        if(o == null){
            return response;
            // 如果 o 已经是 CommonResponse, 不需要再次处理
        } else if (o instanceof CommonResponse){
            response = (CommonResponse<Object>) o;
        } else {
            response.setData(o);
        }
        return response;
    }
}
