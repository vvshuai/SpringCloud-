package org.vvs.coupon.filter;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: vvshuai
 * @Description: 限流过滤器
 * @Date: Created in 20:33 2020/7/1
 * @Modified By:
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class RateLimiterFilter extends AbstractPreZuulFilter{

    /**
     * @Description: 限流器，2.0--->每秒获取到两个令牌
     * @return:
     */
    RateLimiter rateLimiter = RateLimiter.create(2.0);

    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        //尝试获取令牌
        if(rateLimiter.tryAcquire()){
            log.info("get rate token success");
            return success();
        }else{
            log.error("rate limit:{}", request.getRequestURI());
            return fail(402, "error: rate limit");
        }
    }

    @Override
    public int filterOrder() {
        return 2;
    }
}
