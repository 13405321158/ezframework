package com.leesky.ezframework.redis.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * desc：分布式redis锁
 * 所有锁业务必须释放锁，防止死锁
 *
 * @author： 魏来
 * @date： 2021/12/1 上午10:44
 */

@Service
public class LockHandler {
    private static final Logger logger = LoggerFactory.getLogger(LockHandler.class);


    /**
     * 最大持有锁的时间(30分)
     */
    private final static long LOCK_EXPIRE = 30 * 60 * 1000L;

    /**
     * 尝试获取锁的时间间隔（毫秒）
     */
    private final static long LOCK_TRY_INTERVAL = 30L;

    /**
     * 获取锁最大等待时间(秒 )
     */
    private final static long LOCK_WAIT_TIME = 20 * 1000L;

    @Autowired
    private RedisTemplate<String, String> template;

    /**
     * 释放锁
     */
    public void unLock(String lockKey) {
        if (!StringUtils.isEmpty(lockKey))
            template.delete(lockKey);
    }

    /**
     * 尝试获取 分布式锁
     *
     * @param lockKey 锁名
     * @return true 得到了锁 ，false 获取锁失败
     */
    public boolean tryLock(String lockKey) {
        return getLock(lockKey, LOCK_WAIT_TIME, LOCK_TRY_INTERVAL, LOCK_EXPIRE);
    }

    /**
     * 尝试获取 分布式锁（不自动释放锁）
     *
     * @param lockKey 锁名
     * @return true 得到了锁 ，false 获取锁失败
     */
    public boolean tryLockNotAutoRelease(String lockKey) {
        return getLock(lockKey, LOCK_WAIT_TIME, LOCK_TRY_INTERVAL, -1);
    }

    /**
     * 尝试获取 分布式锁（加锁 30分钟）
     *
     * @param lockKey  锁名
     * @param waitTime 获取锁最大等待时间
     * @return true 得到了锁 ，false 获取锁失败
     */
    public boolean tryLock(String lockKey, long waitTime) {
        return getLock(lockKey, waitTime, LOCK_TRY_INTERVAL, LOCK_EXPIRE);
    }

    /**
     * 尝试获取 分布式锁（不自动释放锁）
     *
     * @param lockKey  锁名
     * @param waitTime 获取锁最大等待时间
     * @return true 得到了锁 ，false 获取锁失败
     */
    public boolean tryLockNotAutoRelease(String lockKey, long waitTime) {
        return getLock(lockKey, waitTime, LOCK_TRY_INTERVAL, -1);
    }

    /**
     * 尝试获取 分布式锁（加锁 30秒）
     *
     * @param lockKey     锁名
     * @param waitTime    获取锁最大等待时间
     * @param tryInterval 获取锁尝试 时间间隔
     * @return true 得到了锁 ，false 获取锁失败
     */
    public boolean tryLock(String lockKey, long waitTime, long tryInterval) {
        return getLock(lockKey, waitTime, tryInterval, LOCK_EXPIRE);
    }

    /**
     * 尝试获取 分布式锁（永不释放锁:lockExpireTime =-1）
     *
     * @param lockKey     锁名
     * @param timeout     获取锁最大等待时间
     * @param tryInterval 获取锁尝试 时间间隔
     * @return true 得到了锁 ，false 获取锁失败
     */
    public boolean tryLockNotAutoRelease(String lockKey, long waitTime, long tryInterval) {
        return getLock(lockKey, waitTime, tryInterval, -1);
    }


    /**
     * 获取分布式锁
     *
     * @param lockKey        锁名
     * @param timeout        获取锁最大等待时间
     * @param tryInterval    获取锁尝试 时间间隔
     * @param lockExpireTime 锁最大持有时间
     * @return true 得到了锁 ，false 获取锁失败
     */
    public boolean getLock(String lockKey, long waitTime, long tryInterval, long lockExpireTime) {
        try {
            if (StringUtils.isEmpty(lockKey))
                return false;

            long startTime = System.currentTimeMillis();
            do {
                ValueOperations<String, String> ops = template.opsForValue();
                if (Boolean.TRUE.equals(ops.setIfAbsent(lockKey, "lockValue"))) {
                    if (lockExpireTime > 0)
                        template.expire(lockKey, lockExpireTime, TimeUnit.MILLISECONDS);
                    return true;
                }
                Thread.sleep(tryInterval);//线程休眠
            } while (System.currentTimeMillis() - startTime < waitTime);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            return false;
        }
        return false;
    }


}
