package org.vvs.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.vvs.coupon.constant.Constant;
import org.vvs.coupon.constant.CouponStatus;
import org.vvs.coupon.entity.Coupon;
import org.vvs.coupon.exception.CouponException;
import org.vvs.coupon.service.IRedisService;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: vvshuai
 * @Description: 相关的操作服务接口实现
 * @Date: Created in 10:37 2020/12/3
 * @Modified By:
 */
@Slf4j
@Service
public class RedisServiceImpl implements IRedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * @param userId 用户id
     * @param status 状态列表
     * @return
     */
    @Override
    public List<Coupon> getCachedCoupons(Long userId, Integer status) {

        log.info("Get Coupons From Cache: {}, {}", userId, status);

        String redisKey = status2RedisKey(status, userId);

        List<String> couponStrs = redisTemplate.opsForHash().values(redisKey)
                .stream().map(o -> Objects.toString(o, null))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(couponStrs)) {
            saveEmptyCouponListToCache(userId, Collections.singletonList(status));

            return Collections.emptyList();
        }

        return couponStrs.stream()
                .map(cs -> JSON.parseObject(cs, Coupon.class))
                .collect(Collectors.toList());
    }

    /**
     * @Description: 保存空的列表到缓存 避免缓存穿透
     * @return: void
     */
    @Override
    @SuppressWarnings("all")
    public void saveEmptyCouponListToCache(Long userId, List<Integer> status) {

        log.info("Save Empty List To Cache For User: {}, Status: {}",
                userId, JSON.toJSONString(status));

        // key 是 coupon_id, value 是序列化的 Coupon
        Map<String, String> invalidCouponMap = new HashMap<>();
        invalidCouponMap.put("-1", JSON.toJSONString(Coupon.invalidCoupon()));

        // 用户优惠券缓存信息
        // KV
        // K: status -> redisKey
        // V: {coupon_id: 序列化的 Coupon}

        // 使用 SessionCallback 把数据命令放入到 Redis 的 pipeline
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                status.forEach(s -> {

                    String redisKey = status2RedisKey(s, userId);
                    operations.opsForHash().putAll(redisKey, invalidCouponMap);
                });

                return null;
            }
        };

        log.info("Pipeline Exe Result: {}",
                JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));
    }

    @Override
    public String tryToAcquireCouponCodeFromCache(Integer templateId) {

        String redisKey = String.format("%s%s",
                Constant.RedisPrefix.COUPON_TEMPLATE, templateId.toString());

        // 因为优惠券码不存在顺序关系
        String couponCode = redisTemplate.opsForList().leftPop(redisKey);

        log.info("Acquire Coupon Code: {}, {}, {}", templateId, redisKey, couponCode);

        return couponCode;
    }

    /**
     * @Description: 保存到cache中
     *
     * @return: java.lang.Integer
     */
    @Override
    public Integer addCouponToCache(Long userId, List<Coupon> coupons,
                                    Integer status) throws CouponException {

        log.info("Add Coupon To Cache: {}, {}, {}", userId, JSON.toJSONString(coupons), status);

        Integer result = -1;
        CouponStatus couponStatus = CouponStatus.of(status);

        switch (couponStatus) {
            case USABLE:
                result = addCouponToCacheForUsable(userId, coupons);
                break;
            case USED:
                result = addCouponToCacheForUsed(userId, coupons);
                break;
            case EXPIRED:
                result = addCouponToCachedForExpired(userId, coupons);
                break;
        }

        return result;
    }

    /**
     * @Description: 新增加优惠券到 Cache 中
     * @return: java.lang.Integer
     */
    private Integer addCouponToCacheForUsable(Long userId, List<Coupon> coupons) {

        // 如果 status 是 USABLE，代表是新增加的优惠券
        // 只会影响 Cache
        log.debug("Add Coupon To Cache For Usable.");

        Map<String, String> needCachedObject = new HashMap<>();
        coupons.forEach(c -> needCachedObject .put(
                c.getId().toString(), JSON.toJSONString(c)
        ));

        String redisKey = status2RedisKey(CouponStatus.USABLE.getCode(), userId);

        redisTemplate.opsForHash().putAll(redisKey, needCachedObject);

        log.info("Add {} Coupons To Cache: {}, {}", needCachedObject.size(), userId, redisKey);

        redisTemplate.expire(redisKey, getRandomExpirationTime(1, 2), TimeUnit.SECONDS);

        return needCachedObject.size();
    }

    /**
     * @Description: 将已使用的优惠券加入到Cache中
     * @return: java.lang.Integer
     */
    @SuppressWarnings("all")
    private Integer addCouponToCacheForUsed(Long userId, List<Coupon> coupons)
        throws CouponException{

        // 如果status 是 USED 代表用户操作是使用当前优惠券
        log.debug("Add Coupon To Cache For Used.");

        Map<String, String> needCachedForUsed = new HashMap<>(coupons.size());

        String redisKeyForUsable = status2RedisKey(CouponStatus.USABLE.getCode(), userId);
        String redisKeyForUsed = status2RedisKey(CouponStatus.USED.getCode(), userId);

        // 获取当前用户可用的优惠券
        List<Coupon> curUsableCoupons = getCachedCoupons(
                userId, CouponStatus.USABLE.getCode()
        );
        // 当前可用的优惠券个数一定是大于1的
        assert curUsableCoupons.size() > coupons.size();

        coupons.forEach(c -> needCachedForUsed.put(c.getId().toString(), JSON.toJSONString(c)));

        //校验当前的优惠券参数是否与 Cached 中匹配
        List<Integer> curUsableIds = curUsableCoupons.stream()
                .map(Coupon::getId).collect(Collectors.toList());
        List<Integer> paramIds = coupons.stream()
                .map(Coupon::getId).collect(Collectors.toList());
        // 如果 a 是 b 的子集
        if(!CollectionUtils.isSubCollection(paramIds, curUsableIds)) {
            log.error("CurCoupon Is Not Equal ToCache:{}, {}, {}",
                    userId, JSON.toJSONString(curUsableIds),
                    JSON.toJSONString(paramIds));
            throw new CouponException("CurCoupons Is Not Equal To Cache!");
        }

        List<String> needCleanKey = paramIds.stream()
                .map(i -> i.toString())
                .collect(Collectors.toList());

        SessionCallback<Objects> sessionCallback = new SessionCallback<Objects>() {
            @Override
            public Objects execute(RedisOperations redisOperations) throws DataAccessException {
                // 1. 已使用的优惠券
                redisOperations.opsForHash().putAll(redisKeyForUsed, needCachedForUsed);

                // 2. 可用的优惠券 Cache 需要清理
                redisOperations.opsForHash().delete(
                        redisKeyForUsable, needCleanKey.toArray()
                );

                // 3. 清理完成后 重置过期时间
                redisOperations.expire(
                        redisKeyForUsable,
                        getRandomExpirationTime(1, 2),
                        TimeUnit.SECONDS
                );
                redisOperations.expire(
                        redisKeyForUsed,
                        getRandomExpirationTime(1, 2),
                        TimeUnit.SECONDS
                );
                return null;
            }
        };
        log.info("Pipeline Exec Result:{}",
                JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));

        return coupons.size();
    }

    /**
     * @Description: 将过期优惠券加入到 Cache 中
     * @return: java.lang.Integer
     */
    @SuppressWarnings("all")
    private Integer addCouponToCachedForExpired(Long userId, List<Coupon> coupons)
        throws CouponException {

        // EXPIRED 代表已有的优惠券过期 影响到两个Cache
        // USABLE EXPIRED
        log.debug("Add Coupon To Cache For Expired.");

        // 最终需要保存的 Cache
        Map<String, String> needCachedForExpired = new HashMap<>(coupons.size());

        String redisKeyForUsable = status2RedisKey(
                CouponStatus.USABLE.getCode(), userId
        );
        String redisKeyForExpired = status2RedisKey(
                CouponStatus.EXPIRED.getCode(), userId
        );

        List<Coupon> curUsableCoupons = getCachedCoupons(
                userId, CouponStatus.USABLE.getCode()
        );
        List<Coupon> curExpiredCoupons = getCachedCoupons(
                userId, CouponStatus.EXPIRED.getCode()
        );

        assert  curUsableCoupons.size() > coupons.size();

        coupons.forEach(c -> needCachedForExpired.put(
                c.getId().toString(),
                JSON.toJSONString(c)
        ));

        // 校验当前的优惠券参数 是否与 Cached 中的匹配
        List<Integer> curUsableIds = curUsableCoupons.stream()
                .map(Coupon::getId).collect(Collectors.toList());

        List<Integer> paramIds = coupons.stream()
                .map(Coupon::getId).collect(Collectors.toList());

        if (CollectionUtils.isSubCollection(paramIds, curUsableIds)) {
            log.error("CurCoupons Is Not Equal To Cache: {}, {}, {}",
                    userId, JSON.toJSONString(curUsableIds), JSON.toJSONString(paramIds));
            throw  new CouponException("CurCoupons Is Not Equal To Cache");
        }

        List<String> needCleanKey = paramIds.stream()
                .map(i -> i.toString()).collect(Collectors.toList());

        SessionCallback<Objects> sessionCallback = new SessionCallback<Objects>() {
            @Override
            public Objects execute(RedisOperations redisOperations) throws DataAccessException {
                // 1.已经过期的优惠券
                redisOperations.opsForHash().putAll(redisKeyForExpired, needCachedForExpired);

                // 2.减去已经使用的优惠券
                redisOperations.opsForHash().delete(redisKeyForUsable, needCleanKey.toArray());

                redisOperations.expire(
                        redisKeyForUsable,
                        getRandomExpirationTime(1, 2),
                        TimeUnit.SECONDS
                );
                redisOperations.expire(
                        redisKeyForExpired,
                        getRandomExpirationTime(1, 2),
                        TimeUnit.SECONDS
                );

                return null;
            }
        };
        log.info("Pipe Exec Result: {}", JSON.toJSONString(
                redisTemplate.executePipelined(sessionCallback)
        ));

        return coupons.size();
    }

    /**
     * @Description: 根据status获取到对应的redis key
     * @return: java.lang.String
     */
    private String status2RedisKey(Integer status, Long userId) {

        String redisKey = null;
        CouponStatus couponStatus = CouponStatus.of(status);

        switch (couponStatus) {
            case USABLE:
                redisKey = String.format("%s%s",
                        Constant.RedisPrefix.USER_COUPON_USABLE,userId);
                break;
            case USED:
                redisKey = String.format("%s%s",
                        Constant.RedisPrefix.USER_COUPON_USED, userId);
                break;
            case EXPIRED:
                redisKey = String.format("%s%s",
                        Constant.RedisPrefix.USER_COUPON_EXPIRED, userId);
                break;
        }
        return redisKey;
    }

    /**
     * @Description: 获取一个随机的过期时间
     * 解决缓存雪崩
     * min -> 最小的小时数
     * max -> 最大的小时数
     * 返回一个随机的秒数
     * @return:
     */
    private Long getRandomExpirationTime(Integer min, Integer max) {

        return RandomUtils.nextLong(
                min * 60 * 60,
                max * 60 * 60
        );
    }
}
