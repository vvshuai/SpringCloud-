package org.vvs.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 23:44 2020/12/2
 * @Modified By:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfo {

    /** 类型 */
    private Integer type;

    /** 商品价格 */
    private Double price;

    /** 商品数量 */
    private Integer count;

    //TODO 名称， 使用信息
}
