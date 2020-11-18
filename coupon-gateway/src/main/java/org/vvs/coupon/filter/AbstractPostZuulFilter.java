package org.vvs.coupon.filter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 20:04 2020/7/1
 * @Modified By:
 */
public abstract class AbstractPostZuulFilter extends AbstractZuulFilter{

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }
}
