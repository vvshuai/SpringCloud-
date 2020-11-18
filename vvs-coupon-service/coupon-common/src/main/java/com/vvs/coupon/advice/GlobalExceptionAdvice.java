package com.vvs.coupon.advice;

import com.vvs.coupon.exception.CouponException;
import com.vvs.coupon.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: vvshuai
 * @Description: 全局异常处理
 * @Date: Created in 18:43 2020/7/2
 * @Modified By:
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * @Description: 对CouponException进行统一处理
     * @return: com.vvs.coupon.vo.CommonResponse<java.lang.String>
     */
    @ExceptionHandler(value = CouponException.class)
    public CommonResponse<String> handlerCouponException(HttpServletRequest request, CouponException ex){
        CommonResponse<String> response = new CommonResponse<>(
            -1, "business error"
        );
        response.setData(ex.getMessage());
        return response;
    }
}
