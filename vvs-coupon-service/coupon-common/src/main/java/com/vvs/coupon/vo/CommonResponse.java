package com.vvs.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: vvshuai
 * @Description: 通用响应对象定义
 * @Date: Created in 17:58 2020/7/2
 * @Modified By:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements Serializable {

    private Integer code;
    private String message;
    private T data;

    public CommonResponse(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
