///*
// * @作者: 魏来
// * @日期: 2021年12月3日  下午3:17:46
// * @组织: 森麒麟轮胎股份有限公司.
// * @部门: 国内市场替换部IT组
// * @描述: 从redis中获取token
// */
//package com.leesky.ezframework.auth.utils;
//
//import com.google.common.collect.ImmutableMap;
//import com.leesky.ezframework.global.Redis;
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.lang3.ObjectUtils;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
//import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
//
//import java.math.BigInteger;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Map;
//
//
//@Deprecated
////@Configuration
//@RequiredArgsConstructor
//public class TokenHandle {
//
//    private final RedisConnectionFactory connectionFactory;
//
//    private static final String AUTH_TO_ACCESS = "auth_to_access:";
//
//    private final RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();
//
//    public OAuth2AccessToken getAccessToken(String username) {
//        byte[] bytes;
//        String key = generateKey(ImmutableMap.of("username", username, "client_id", username, "scope", "all"));
//        byte[] serializedKey = serializeKey(AUTH_TO_ACCESS + key);
//
//        try (RedisConnection conn = getConnection()) {
//            bytes = conn.get(serializedKey);
//        }
//        return ObjectUtils.isNotEmpty(bytes) ? deserializeAccessToken(bytes) : null;
//
//    }
//
//    private byte[] serializeKey(String object) {
//        return serializationStrategy.serialize(Redis.AUTH_TOKEN + object);
//    }
//
//    private RedisConnection getConnection() {
//        return connectionFactory.getConnection();
//    }
//
//    private OAuth2AccessToken deserializeAccessToken(byte[] bytes) {
//        return serializationStrategy.deserialize(bytes, OAuth2AccessToken.class);
//    }
//
//    private String generateKey(Map<String, String> values) {
//
//        try {
//            MessageDigest digest = MessageDigest.getInstance("MD5");
//            byte[] bytes = digest.digest(values.toString().getBytes(StandardCharsets.UTF_8));
//            return String.format("%032x", new BigInteger(1, bytes));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//}
