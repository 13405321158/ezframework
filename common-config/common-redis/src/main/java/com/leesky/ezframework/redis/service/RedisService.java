package com.leesky.ezframework.redis.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

/**
 * @author weilai
 * @data 2019年3月27日 下午3:12:03
 * @desc 类描述 ：定义 redis的16个仓库连接
 * <li>下列就是redis其他类型的对应操作方式：
 *
 * <li>opsForValue：对应String字符串
 *
 * <li>opsForZSet：对应ZSet有序集合
 *
 * <li>opsForHash：对应Hash哈希
 *
 * <li>opsForList：对应List列表
 *
 * <li>opsForSet：对应Set集合
 *
 * <li>opsForGeo：对应GEO地理位置
 */

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 写入缓存
     */
    public void add(String key, Object value) {

        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();

            operations.set(key, value);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 写入缓存设置时效时间
     */
    public void add(String key, Object value, Long expireTime) {

        try {

            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 单个删除
     */
    public void del(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 批量删除
     */
    public void del(List<String> keys) {
        for (String key : keys)
            del(key);

    }

    /**
     * 模糊删除
     */
    public void delPattern(String pattern) {
        Cursor<String> cursor = this.scan(pattern);
        while (cursor.hasNext())
            redisTemplate.delete(cursor.next());

        cursor.close();
    }


    /**
     * 判断是否存在
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 读取缓存
     */
    public Object get(String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);

    }

    /**
     * 哈希 添加
     * 键值对集合,即编程语言中的Map类型
     */
    public void addHash(String redisKey, Object mapKey, Object mapValue) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(redisKey, mapKey, mapValue);

    }

    /**
     * 哈希获取数据
     * 键值对集合,即编程语言中的Map类型
     */
    public Object getHash(String redisKey, Object mapKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(redisKey, mapKey);
    }

    /**
     * 批量删除 哈希数据
     */
    public void delHash(String redisKey, List<String> mapKeys) {

        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        for (String str : mapKeys)
            hash.delete(redisKey, str);
    }

    /**
     * 单个删除 哈希数据
     */
    public void delHash(String redisKey, String mapKey) {

        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();

        hash.delete(redisKey, mapKey);

    }

    /**
     * 列表添加
     */
    public void addList(String redisKey, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.leftPush(redisKey, v);
    }

    /**
     * 列表添加
     */
    public void addList(String redisKey, Collection<?> v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.leftPush(redisKey, v);

    }

    /**
     * 列表获取
     */
    public List<Object> getList(String redisKey, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(redisKey, l, l1);
    }

    /**
     * 集合添加
     */
    public void addSet(String redisKey, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(redisKey, value);
    }

    /**
     * 集合获取
     */
    public Set<Object> getSet(String redisKey) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(redisKey);
    }

    /**
     * 有序集合添加
     */
    public void addSetZ(String redisKey, Object value, double score) {
        ZSetOperations<String, Object> set = redisTemplate.opsForZSet();
        set.add(redisKey, value, score);
    }

    /**
     * 有序集合获取
     */
    public Set<Object> getSetZ(String key, double score, double score1) {
        ZSetOperations<String, Object> set = redisTemplate.opsForZSet();
        return set.rangeByScore(key, score, score1);
    }

    /**
     * 序号自增
     */
    public Long getSnCount(String redisKey) {

        return this.redisTemplate.opsForValue().increment(redisKey);
    }




    @SuppressWarnings({"unchecked", "rawtypes"})
    private Cursor<String> scan(String match) {
        ScanOptions scanOptions = ScanOptions.scanOptions().match(match).count(500).build();
        return (Cursor) redisTemplate.executeWithStickyConnection(

                (RedisCallback) redisConnection -> new ConvertingCursor<>(

                        redisConnection.scan(scanOptions), redisTemplate.getKeySerializer()::deserialize)

        );
    }


}
