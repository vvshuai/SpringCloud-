package org.vvs.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: vvshuai
 * @Description: 过滤器中存储客户端发起的时间戳
 * @Date: Created in 20:46 2020/7/1
 * @Modified By:
 */
@Slf4j
@Component
public class PreRequestFilter extends AbstractPreZuulFilter{
    @Override
    protected Object cRun() {
        context.set("startTime", System.currentTimeMillis());
        return success();
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
