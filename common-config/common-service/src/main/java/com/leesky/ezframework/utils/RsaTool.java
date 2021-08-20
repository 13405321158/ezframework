/*
 * @author weilai
 * @desc RSACoder.java 是使用证书 生成公钥和私钥，下面推荐一个使用API生成公钥和私钥
 * <p>
 * <p>
 * 1、公钥加密，私钥解密用于信息加密
 * 2、私钥加密，公钥解密用于数字签名
 */
package com.leesky.ezframework.utils;

import com.google.common.collect.Maps;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

public class RsaTool {

    private final static int MAX_KEY = 2048;
    private final static int MAX_ENCRYPT_BLOCK = 117;
    private final static int MAX_DECRYPT_BLOCK = MAX_KEY / 8;

    private static final String KEY_RSA = "RSA";
    private static final String KEY_RSA_SIGNATURE = "MD5withRSA";
    private static final String KEY_RSA_PUBLIC = "RSAPublicKey";
    private static final String KEY_RSA_PRIVATE = "RSAPrivateKey";


    /**
     * 生成公私密钥对
     */
    public static Map<String, String> init() {
        Map<String, String> map = Maps.newHashMap();
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_RSA);

            generator.initialize(MAX_KEY, new SecureRandom());
            KeyPair keyPair = generator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            map.put(KEY_RSA_PUBLIC, encryptBase64(publicKey.getEncoded()));
            map.put(KEY_RSA_PRIVATE, encryptBase64(privateKey.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return map;
    }

//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓公钥加密，私钥解密↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * 公钥加密: 分段加密
     */
    public static String encryptByPublicKey(String str, String publicKeyStr) {
        int offSet = 0, i = 0;

        byte[] cache, decryptedData = null;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] data = str.getBytes(StandardCharsets.UTF_8);
            byte[] publicKeyBytes = decryptBase64(publicKeyStr);

            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PublicKey publicKey = factory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int inputLen = data.length;

            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            decryptedData = out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptBase64(decryptedData);
    }

    /**
     * 私钥解密: 分段解密
     */
    public static String decryptByPrivateKey(String str, String privateKeyStr) {
        int offSet = 0, i = 0;
        byte[] cache, decryptedData;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] privateKeyBytes = decryptBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            byte[] data = decryptBase64(str);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            while (data.length - offSet > 0) {
                if (data.length - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, data.length - offSet);
                }

                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            decryptedData = out.toByteArray();

            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑公钥加密，私钥解密↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓私钥加密，公钥解密↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * 私钥加密 分段加密
     */
    public static String encryptByPrivateKey(String str, String privateKeyStr) {
        int offSet = 0, i = 0;
        byte[] cache, decryptedData;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] privateKeyBytes = decryptBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PrivateKey privateKey = factory.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] data = str.getBytes(StandardCharsets.UTF_8);


            while (data.length - offSet > 0) {
                if (data.length - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, data.length - offSet);
                }

                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            return encryptBase64(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 公钥解密 分段解密
     */
    public static String decryptByPublicKey(String str, String publicKeyStr) {
        int offSet = 0, i = 0;
        byte[] cache, decryptedData;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] publicKeyBytes = decryptBase64(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PublicKey publicKey = factory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            byte[] data = decryptBase64(str);
            int inputLen = data.length;

            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            decryptedData = out.toByteArray();

            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑私钥加密，公钥解密↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓数字签名↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * @描述: 私钥生成签名
     * @描述: encryptedStr是使用私钥加密过的字符串
     */
    public static String sign(String encryptedStr, String privateKey) {
        String sign = "";
        try {
            byte[] data = encryptedStr.getBytes();
            byte[] bytes = decryptBase64(privateKey);
            PKCS8EncodedKeySpec pkcs = new PKCS8EncodedKeySpec(bytes);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PrivateKey key = factory.generatePrivate(pkcs);
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initSign(key);
            signature.update(data);
            sign = encryptBase64(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

    /**
     * 公钥验证 校验数字签名
     *
     * @return 校验成功返回true，失败返回false
     * @描述: encryptedStr 用私钥加工过的字符串
     */
    public static boolean verify(String encryptedStr, String publicKey, String sign) {
        boolean flag = false;
        try {
            byte[] data = encryptedStr.getBytes();
            byte[] bytes = decryptBase64(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PublicKey key = factory.generatePublic(keySpec);
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initVerify(key);
            signature.update(data);
            flag = signature.verify(decryptBase64(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * BASE64 解码
     *
     * @param key 需要Base64解码的字符串
     * @return 字节数组
     */
    private static byte[] decryptBase64(String key) {
        return Base64.getDecoder().decode(key);
    }

    /**
     * BASE64 编码
     *
     * @param key 需要Base64编码的字节数组
     */
    private static String encryptBase64(byte[] key) {
        return new String(Base64.getEncoder().encode(key));
    }


}
