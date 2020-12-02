package org.vvs.coupon.service.impl;

import org.vvs.coupon.exception.CouponException;
import org.vvs.coupon.vo.CouponTemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vvs.coupon.dao.CouponTemplateDao;
import org.vvs.coupon.entity.CouponTemplate;
import org.vvs.coupon.service.ITemplateBaseService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: vvshuai
 * @Description: 优惠券模板基础服务
 * @Date: Created in 23:23 2020/11/30
 * @Modified By:
 */
@Service
@Slf4j
public class TemplateBaseServiceImpl implements ITemplateBaseService {

    /** CouponTemplate Dao */
    private final CouponTemplateDao templateDao;

    @Autowired
    public TemplateBaseServiceImpl(CouponTemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    @Override
    public CouponTemplate buildTemplateInfo(Integer id) throws CouponException {

        Optional<CouponTemplate> template = templateDao.findById(id);
        if (!template.isPresent()) {
            throw new CouponException("Template Is Not Exist!" + id);
        }

        return template.get();
    }

    @Override
    public List<CouponTemplateSDK> findAllUsalbeTemplate() {

        List<CouponTemplate> templates =
                templateDao.findAllByAvailableAndExpired(
                        true, false);

        return templates.stream()
                .map(this::template2TemplateSDK)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, CouponTemplateSDK> findIds2TemplateSDK(Collection<Integer> ids) {

        List<CouponTemplate> templates = templateDao.findAllById(ids);

        return templates.stream().map(this::template2TemplateSDK).collect(Collectors.toMap(
                CouponTemplateSDK::getId, Function.identity()
        ));
    }

    /**
     * @Description: 属性转换器
     * @return: com.vvs.coupon.vo.CouponTemplateSDK
     */
    private CouponTemplateSDK template2TemplateSDK(CouponTemplate couponTemplate) {

        return new CouponTemplateSDK(
                couponTemplate.getId(),
                couponTemplate.getName(),
                couponTemplate.getLogo(),
                couponTemplate.getDesc(),
                couponTemplate.getCategory().getCode(),
                couponTemplate.getProductLine().getCode(),
                couponTemplate.getKey(), // 不是拼装好的key
                couponTemplate.getTarget().getCode(),
                couponTemplate.getRule()
        );
    }
}
