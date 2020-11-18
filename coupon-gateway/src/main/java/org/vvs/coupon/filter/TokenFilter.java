package org.vvs.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: vvshuai
 * @Description: 校验请求中传递的token的filter
 * @Date: Created in 20:05 2020/7/1
 * @Modified By:
 */
@Component
@Slf4j
public class TokenFilter extends AbstractPreZuulFilter{

    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        log.info(String.format("%s request to %s",
                request.getMethod(), request.getRequestURL().toString()));
        Object token = request.getParameter("token");
        if (token == null){
            log.error("error: token is empty");
            return fail(401, "error: token is empty");
        }
        return success();
    }

    /**
     * @Description: 数字越小越高
     * @return:
     */
    @Override
    public int filterOrder() {
        return 1;
    }
}
