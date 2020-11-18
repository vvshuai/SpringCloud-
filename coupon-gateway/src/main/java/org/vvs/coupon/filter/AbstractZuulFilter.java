package org.vvs.coupon.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * @Author: vvshuai
 * @Description:
 * @Date: Created in 14:38 2020/7/1
 * @Modified By:
 */
public abstract class AbstractZuulFilter extends ZuulFilter {

    /**
     * @Description:过滤器间传递消息，数据保存在ThreadLocal中
     * 扩展了ConcurrentHashMap
     */
    RequestContext context;

    private static final String NEXT = "next";

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (boolean) ctx.getOrDefault(NEXT, true);
    }

    @Override
    public Object run() throws ZuulException {

        context = RequestContext.getCurrentContext();
        return cRun();
    }

    /**
     * @Description: 转换到run方法
     * @return:
     */
    protected abstract Object cRun();

    /**
     * @Description: 出现错误立刻返回
     * @return: java.lang.Object
     */
    Object fail(int code, String msg){
        context.set(NEXT, false);
        context.setSendZuulResponse(false);
        context.getResponse().setContentType("text/html;charset=UTF-8");
        context.setResponseStatusCode(code);
        context.setResponseBody(String.format("{\"result\":\"%s!\"}" , msg));
        return null;
    }

    Object success() {
        context.set(NEXT, true);

        return null;
    }
}
