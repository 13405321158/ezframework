package com.leesky.ezframework.redis.config;

import lombok.Getter;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: PangYanNan
 * @date: 2021-06-04
 * @Description:
 */
@Getter
public class RedisTemplateJdk{
    private RedisTemplate<String,Object>  redisTemplate;
    public RedisTemplateJdk(RedisTemplate<String,Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }
}
