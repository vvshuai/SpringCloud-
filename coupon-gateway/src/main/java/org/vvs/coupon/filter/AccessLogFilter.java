package org.vvs.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 20:51 2020/7/1
 * @Modified By:
 */
@Slf4j
@Component
public class AccessLogFilter extends AbstractPostZuulFilter{
    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        //从PreRequestFilter中设置请求时间戳
        Long startTime = (Long) context.get("startTime");
        String uri = request.getRequestURI();
        long duration = System.currentTimeMillis() - startTime;

        //从网关通过的请求都会打印日志记录：uri + duration
        log.info("uri : {}, duration: {}", uri, duration);
        return success();
    }
    
    /**
     * @Description: 返回指定参数
     * @return: int
     */
    @Override
    public int filterOrder() {
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }
}
