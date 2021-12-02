/**
 * @author: weilai
 * @Data:2020年5月4日上午9:34:12
 * @Org:Sentury Co., ltd.
 * @Deparment:Domestic Sales, Tech Center
 * @Desc: <li>
 */
package com.leesky.ezframework.redis.config;

import com.google.common.collect.Sets;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.Set;

@Configuration
@EnableCaching // 开启缓存配置，在其它类上可以使用@Cacheable @CachePut @CacheEvict 等注解
@RequiredArgsConstructor
public class RedisConfig extends CachingConfigurerSupport {

    private final RedisPropertiesConfig redisProperties;

    /**
     * @Desc: <li>根据配置(redis.pojo),决定redis使用哪种模式
     * <li>standalone:单机
     * <li>sentinel:集群哨兵模式
     * <li>cluster: 集群cluster模式
     * @author:weilai
     * @Data:2020年5月4日 上午9:50:49
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory factory = null;
        String model = redisProperties.getModel();

        switch (model) {

            case "sentinel":
                factory = new LettuceConnectionFactory(redisSentinelConfiguration(), lettucePoolConfig());
                break;

            case "cluster":
                factory = new LettuceConnectionFactory(redisClusterConfiguration(), lettucePoolConfig());
                break;

            case "standalone":
                factory = new LettuceConnectionFactory(redisStandaloneConfiguration(), lettucePoolConfig());
                break;
        }

        return factory;
    }

    /**
     * <li>集群模式01：哨兵配置
     *
     * @author: weilai
     * @Data:2020年5月4日 上午9:47:31
     */
    @Bean
    public RedisSentinelConfiguration redisSentinelConfiguration() {
        Set<RedisNode> sentinels = Sets.newHashSet();

        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration();
        sentinelConfig.setMaster(redisProperties.getMaster());

        String[] host = redisProperties.getRedisNodes().split(",");
        for (String redisHost : host) {
            String[] item = redisHost.split(":");
            String ip = item[0].trim();
            String port = item[1].trim();
            sentinels.add(new RedisNode(ip, Integer.parseInt(port)));
        }
        sentinelConfig.setSentinels(sentinels);
        sentinelConfig.setDatabase(redisProperties.getDatabase());

        sentinelConfig.setPassword(RedisPassword.of(redisProperties.getPwd()));

        return sentinelConfig;
    }

    /**
     * <li>集群模式02：cluster方式配置
     *
     * @author: weilai
     * @Data:2020年5月4日 上午9:47:31
     */
    public RedisClusterConfiguration redisClusterConfiguration() {
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
        Set<RedisNode> nodes = Sets.newHashSet();

        String[] host = redisProperties.getRedisNodes().split(",");
        for (String redisHost : host) {
            String[] item = redisHost.split(":");
            String ip = item[0].trim();
            String port = item[1].trim();
            nodes.add(new RedisNode(ip, Integer.parseInt(port)));
        }

        clusterConfig.setClusterNodes(nodes);
        clusterConfig.setMaxRedirects(5);

        clusterConfig.setPassword(RedisPassword.of(redisProperties.getPwd()));

        return clusterConfig;

    }

    /**
     * <li>单节点配置
     *
     * @author: weilai
     * @Data:2020年5月4日 上午9:47:43
     */
    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration standConfig = new RedisStandaloneConfiguration();
        standConfig.setHostName(redisProperties.getHost());
        standConfig.setPort(redisProperties.getPort());
        standConfig.setDatabase(redisProperties.getDatabase());
        standConfig.setPassword(RedisPassword.of(redisProperties.getPwd()));
        return standConfig;
    }

    /**
     * @author: weilai
     * @Data:2020年5月4日 上午9:47:56
     * @Desc: <li>lettuce 连接池配置
     */
    @Bean
    public LettucePoolingClientConfiguration lettucePoolConfig() {
        GenericObjectPoolConfig<LettuceClientConfiguration> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(redisProperties.getMaxActive());
        poolConfig.setMinIdle(redisProperties.getMinIdle());
        poolConfig.setMaxIdle(redisProperties.getMaxIdle());
        poolConfig.setMaxWaitMillis(redisProperties.getMaxWait());

        poolConfig.setTimeBetweenEvictionRunsMillis(redisProperties.getTimeBetweenEvictionRunsMillis());

        // 支持自适应集群拓扑刷新和静态刷新源
        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions.builder().enablePeriodicRefresh().enableAllAdaptiveRefreshTriggers().refreshPeriod(Duration.ofSeconds(5)).build();

        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder().topologyRefreshOptions(clusterTopologyRefreshOptions).build();
        // 从优先，读写分离，读从可能存在不一致，最终一致性CP
        return LettucePoolingClientConfiguration.builder().clientOptions(clusterClientOptions).poolConfig(poolConfig).readFrom(ReadFrom.REPLICA_PREFERRED).build();

    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setEnableTransactionSupport(false);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);

        return redisTemplate;

    }

    /**
     * 创建JDK的序列化模板，由于RedisTemplate 已经在其他类注入，所以新增一个包装类，防止bean注入重复
     *
     * @return
     */
    @Bean
    public RedisTemplateJdk redisTemplateJdk() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setEnableTransactionSupport(false);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return new RedisTemplateJdk(redisTemplate);

    }

    /**
     * @ver: 1.0.0
     * @author: weilai
     * @data:下午4:26:01,2019年12月2日
     * @desc: <li>定义缓存过期时间，ps此配置仅仅适用于方法上使用@Cacheable的方式
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(60 * 24 * 3)); //设置 过期时间 3天
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        return new RedisCacheManager(redisCacheWriter, config);
    }


}
