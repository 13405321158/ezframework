/**
 * 
 * @author:weilai
 * @Data:2020年5月4日上午9:36:04
 * @Org:Sentury Co.,ltd.
 * @Deparment:Domestic Sales,Tech Center
 * @Desc:
 *        <li>
 */
package com.leesky.ezframework.redis.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class RedisPropertiesConfig {

	@Value("${redis.database:0}")
	private int database;

	@Value("${spring.redis.sentinel.nodes}")
	private String redisNodes;
	
	@Value("${spring.redis.sentinel.master}")
	private String master;

	//////////////////////// redis pool配置
	@Value("${spring.redis.lettuce.pool.max_idle:500}")
	private Integer maxIdle;
	
	@Value("${spring.redis.lettuce.pool.min_idle:200}")
	private Integer minIdle;
	
	@Value("${spring.redis.lettuce.pool.max_active:2000}")
	private Integer maxActive;
	
	@Value("${spring.redis.lettuce.pool.max_wait:5000}")
	private Integer maxWait;
	
	@Value("${spring.redis.lettuce.pool.timeBetweenEvictionRunsMillis:6000}")
	private Integer timeBetweenEvictionRunsMillis;// test idle 线程的时间间隔\



	///////////////////////// redis 单节点配置
	@Value("${redis.model}")
	private String model; // redis使用哪种模式：集群或单机;  如果是集群是哨兵、cluster、主从
	
	@Value("${spring.redis.host}")
	private String host;
	
	@Value("${spring.redis.port}")
	private Integer port;
	
	@Value("${spring.redis.password}")
	private String pwd;
}
