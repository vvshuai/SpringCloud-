package org.vvs.coupon.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vvs.coupon.exception.CouponException;
import org.vvs.coupon.executor.ExecuteManager;
import org.vvs.coupon.vo.SettlementInfo;

/**
 * @Author: vvshuai
 * @Description: 结算服务的 Controller
 * @Date: Created in 21:01 2020/12/8
 * @Modified By:
 */
@Slf4j
@RestController
public class SettlementController {

    /** 结算规则执行管理器 */
    private final ExecuteManager executeManager;

    @Autowired
    public SettlementController(ExecuteManager executeManager) {
        this.executeManager = executeManager;
    }

    /**
     * @Description: 优惠券结算
     * @return: org.vvs.coupon.vo.SettlementInfo
     */
    @PostMapping("/settlement/compute")
    public SettlementInfo computeRule(@RequestBody SettlementInfo settlement)
            throws CouponException{

        log.info("settlement: {}", JSON.toJSONString(settlement));

        return executeManager.computeRule(settlement);
    }
}
