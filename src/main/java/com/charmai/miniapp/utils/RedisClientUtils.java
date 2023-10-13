package com.charmai.miniapp.utils;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Xie
 * @Date: 2023-07-30-17:03
 * @Description:
 */
@Component
@Slf4j
public class RedisClientUtils {

    @Autowired
    RedissonClient redissonClient;

    private static RedisClientUtils redisClientUtils;

    @PostConstruct
    private void init() {
        redisClientUtils = this;
        redisClientUtils.redissonClient = this.redissonClient;

    }

    /**
     * 默认过期时间是30s,每10秒自动续期一次
     *
     * @param lockName
     * @param waitTime
     * @param timeUnit
     * @return
     */
    public static Boolean tryLock(String lockName, long waitTime, TimeUnit timeUnit) {
        RLock lock = redisClientUtils.redissonClient.getLock(lockName);

        try {

            log.info("thread : {} DistributeRedisLock tryLock lockName : {} ", Thread.currentThread().getName(), lockName);
            return lock.tryLock(waitTime, timeUnit);
        } catch (Exception e) {
            log.error("DistributeRedisLock tryLock : {} exception is : {} ", lockName, e.toString());
            return false;
        }
    }

    /**
     * @param lockName
     * @return
     */
    public static Boolean unLock(String lockName) {
        try {
            RLock lock = redisClientUtils.redissonClient.getLock(lockName);
            if (lock.isLocked()) {

                log.info("Thread {} DistributeRedisLock unlock lockName {} before", Thread.currentThread().getName(), lockName);
                lock.unlock();

            }
            return true;
        } catch (Exception e) {
            log.info("Thread {} DistributeRedisLock unlock lockName {} Exception {}", Thread.currentThread().getName(), lockName, e.toString());

            return false;
        }

    }


}
