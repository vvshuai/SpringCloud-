package org.vvs.coupon.service.impl;

import org.vvs.coupon.exception.CouponException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vvs.coupon.dao.CouponTemplateDao;
import org.vvs.coupon.entity.CouponTemplate;
import org.vvs.coupon.service.IAsyncService;
import org.vvs.coupon.service.IBuildTemplateService;
import org.vvs.coupon.vo.TemplateRequest;

/**
 * @Author: vvshuai
 * @Description: 构建优惠券模板接口实现
 * @Date: Created in 22:28 2020/11/30
 * @Modified By:
 */
@Slf4j
@Service
public class BuildTemplateServiceImpl implements IBuildTemplateService {

    /** 异步服务 */
    private final IAsyncService asyncService;

    /** 数据库接口 */
    private final CouponTemplateDao templateDao;

    @Autowired
    public BuildTemplateServiceImpl(CouponTemplateDao templateDao, IAsyncService asyncService) {
        this.templateDao = templateDao;
        this.asyncService = asyncService;
    }

    /**
     * @Description: 创建优惠券模板
     * @return: org.vvs.coupon.entity.CouponTemplate
     */
    @Override
    public CouponTemplate buildTemplate(TemplateRequest request)
            throws CouponException {

        // 参数合法校验
        if (!request.validate()) {
            throw new CouponException("BuildTemplate Param Is Not Valid!");
        }

        // 判断同名的优惠券模板是否存在
        if (null != templateDao.findByName(request.getName())) {
            throw new CouponException("Exist Same Name Template!");
        }

        // 构造 CouponTemplate 保存到数据库中
        CouponTemplate template = requestToTemplate(request);
        template = templateDao.save(template);

        // 根据优惠券模板异步生成
        asyncService.asyncConstructCouponByTemplate(template);

        return template;
    }

    /**
     * @Description: 将 TemplateRequest 转换为 CouponTemplate
     * @return: org.vvs.coupon.entity.CouponTemplate
     */
    private CouponTemplate requestToTemplate(TemplateRequest request) {

        return new CouponTemplate(
                request.getName(),
                request.getLogo(),
                request.getDesc(),
                request.getCategory(),
                request.getProductLine(),
                request.getCount(),
                request.getUserId(),
                request.getTarget(),
                request.getRule()
        );
    }
}
