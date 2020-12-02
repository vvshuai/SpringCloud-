package org.vvs.coupon.controller;

import com.alibaba.fastjson.JSON;
import org.vvs.coupon.exception.CouponException;
import org.vvs.coupon.vo.CouponTemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.vvs.coupon.entity.CouponTemplate;
import org.vvs.coupon.service.IBuildTemplateService;
import org.vvs.coupon.service.ITemplateBaseService;
import org.vvs.coupon.vo.TemplateRequest;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Author: vvshuai
 * @Description: 优惠券模板相关功能控制器
 * @Date: Created in 17:03 2020/12/1
 * @Modified By:
 */
@Slf4j
@RestController
public class CouponTemplateController {

    /** 构建优惠券模板服务 */
    private final IBuildTemplateService buildTemplateService;

    /** 基础服务 */
    private final ITemplateBaseService templateBaseService;

    @Autowired
    public CouponTemplateController(IBuildTemplateService buildTemplateService, ITemplateBaseService templateBaseService) {
        this.buildTemplateService = buildTemplateService;
        this.templateBaseService = templateBaseService;
    }

    /**
     * @Description: 构建优惠券模板
     * localhost:7001/coupon-template/template/build
     * @return: org.vvs.coupon.entity.CouponTemplate
     */
    @PostMapping("/template/build")
    public CouponTemplate buildTemplate(@RequestBody TemplateRequest request)
            throws CouponException {

        log.info("Build Template: {}", JSON.toJSONString(request));

        return buildTemplateService.buildTemplate(request);
    }

    /**
     * @Description: 构造优惠券模板详情
     * @return: org.vvs.coupon.entity.CouponTemplate
     */
    @GetMapping("/template/info")
    public CouponTemplate buildTemplateInfo(@RequestParam("id") Integer id)
            throws CouponException {

        log.info("Build Template Info For: {}", id);

        return templateBaseService.buildTemplateInfo(id);
    }

    /**
     * @Description: 查找所有可用的优惠券模板
     * @return: java.util.List<com.vvs.coupon.vo.CouponTemplateSDK>
     */
    @GetMapping("/template/sdk/all")
    public List<CouponTemplateSDK> findAllUsableTemplate() {
        log.info("Find All Usable Template.");
        return templateBaseService.findAllUsalbeTemplate();
    }

    /**
     * @Description: 获取模板 ids 到 CouponTemplateSDK 的映射
     * @return: java.util.Map<java.lang.Integer,com.vvs.coupon.vo.CouponTemplateSDK>
     */
    @GetMapping("/template/sdk/infos")
    public Map<Integer, CouponTemplateSDK> findIds2TemplateSDK (
        @RequestParam("ids") Collection<Integer> ids
    ) {
        log.info("FindIds2TemplateSDK: {}", JSON.toJSONString(ids));

        return  templateBaseService.findIds2TemplateSDK(ids);
    }
}
