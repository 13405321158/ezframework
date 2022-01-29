/*
 * @author 魏来
 * @desc RSACoder.java 是使用证书 生成公钥和私钥，下面推荐一个使用API生成公钥和私钥
 * 1、公钥加密，私钥解密用于信息加密
 * 2、私钥加密，公钥解密用于数字签名
 */
package com.leesky.ezframework.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.leesky.ezframework.global.Common;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Assert;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@SuppressWarnings("all")
public class RsaTool {


    private final static int MAX_ENCRYPT_BLOCK = 117;

    private static final String KEY_RSA = "RSA";
    private static final String KEY_RSA_SIGNATURE = "MD5withRSA";
    private static final String KEY_RSA_PUBLIC = "RSAPublicKey";
    private static final String KEY_RSA_PRIVATE = "RSAPrivateKey";

//    private static String str="加密过的字符串";
//    public static void main(String[] args) {
//        Map<String, String> map = RsaTool.init(1024);
//        System.out.println("公钥=" + map.get(KEY_RSA_PUBLIC));
//        System.out.println("私钥=" + map.get(KEY_RSA_PRIVATE));
//        String r = RsaTool.decryptByPublicKey(str, Common.RSA_PUBLIC2048, 2048);
//        System.out.println(r);
//    }
//
//    /**
//     * 生成公私密钥对
//     */
//    public static Map<String, String> init(Integer length) {
//        Assert.isTrue(length == 1024 || length == 2048, "参数取值范围[1024,2048]");
//        Map<String, String> map = Maps.newHashMap();
//        try {
//            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_RSA);
//
//            generator.initialize(length, new SecureRandom());
//            KeyPair keyPair = generator.generateKeyPair();
//            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//
//            map.put(KEY_RSA_PUBLIC, encryptBase64(publicKey.getEncoded()));
//            map.put(KEY_RSA_PRIVATE, encryptBase64(privateKey.getEncoded()));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//
//        return map;
//    }

//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓公钥加密，私钥解密↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * 公钥加密: 分段加密
     */
    public static String encryptByPublicKey(String str, String publicKeyStr) {
        int offSet = 0, i = 0;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] data = str.getBytes(StandardCharsets.UTF_8);
            byte[] publicKeyBytes = decryptBase64(publicKeyStr);

            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PublicKey publicKey = factory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] decryptedData = comm02(offSet, i, out, MAX_ENCRYPT_BLOCK, cipher, data);
            return encryptBase64(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 私钥解密: 分段解密
     */
    public static String decryptByPrivateKey(String str, String privateKeyStr, Integer size) {
        Assert.isTrue(size == 1024 || size == 2048, "size必须是1024或2048");

        int offSet = 0, i = 0;

        Integer max_decrypt_block = size / 8;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] privateKeyBytes = decryptBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            byte[] data = decryptBase64(str);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            common(offSet, i, max_decrypt_block, out, data, cipher);
            byte[] decryptedData = out.toByteArray();

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
        byte[] cache;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] privateKeyBytes = decryptBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PrivateKey privateKey = factory.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] data = str.getBytes(StandardCharsets.UTF_8);


            common(offSet, i, MAX_ENCRYPT_BLOCK, out, data, cipher);
            return encryptBase64(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 公钥解密 分段解密
     */
    public static String decryptByPublicKey(String str, String publicKeyStr, Integer size) {
        Assert.isTrue(size == 1024 || size == 2048, "size必须是1024或2048");
        int offSet = 0, i = 0;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Integer max_decrypt_block = size / 8;
            byte[] publicKeyBytes = decryptBase64(publicKeyStr);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PublicKey publicKey = factory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);

            byte[] data = decryptBase64(str);
            byte[] decryptedData = comm02(offSet, i, out, max_decrypt_block, cipher, data);

            return new String(decryptedData, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑私钥加密，公钥解密↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓数字签名↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    /**
     * 私钥生成签名
     * encryptedStr:使用私钥加密过的字符串
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
     * <p>
     * encryptedStr 用私钥加工过的字符串
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
     * 使用私钥(2048位)解密str，饼返回 clz
     */
    public static <T> T decryptString(String str, Class<T> clz) {
        String w = decryptByPrivateKey(str, Common.RSA_PRIVATE2048, 2048);
        return JSON.parseObject(w, clz);
    }

    /**
     * 使用私钥(2048位)解密str，饼返回 List<clz>
     */
    public static <T> List<T> decryptString(List<String> source, Class<T> clz) {
        List<T> result = Lists.newArrayList();
        for (String s : source) {
            if (ObjectUtils.isNotEmpty(s))
                result.add(decryptString(s, clz));
        }
        return result;
    }

    /**
     * BASE64 解码
     * <p>
     * 参数key 需要Base64解码的字符串
     */
    private static byte[] decryptBase64(String key) {
        return Base64.getDecoder().decode(key);
    }

    /**
     * BASE64 编码
     * <p>
     * 参数key 需要Base64编码的字节数组
     */
    private static String encryptBase64(byte[] key) {
        return new String(Base64.getEncoder().encode(key));
    }

    private static void common(int offSet, int i, Integer max_decrypt_block, ByteArrayOutputStream out, byte[] data, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
        byte[] cache;
        while (data.length - offSet > 0) {
            if (data.length - offSet > max_decrypt_block) {
                cache = cipher.doFinal(data, offSet, max_decrypt_block);
            } else {
                cache = cipher.doFinal(data, offSet, data.length - offSet);
            }

            out.write(cache, 0, cache.length);
            i++;
            offSet = i * max_decrypt_block;
        }
    }

    private static byte[] comm02(int offSet, int i, ByteArrayOutputStream out, Integer max_decrypt_block, Cipher cipher, byte[] data) throws IllegalBlockSizeException, BadPaddingException {
        byte[] cache;
        byte[] decryptedData;
        int inputLen = data.length;

        while (inputLen - offSet > 0) {
            if (inputLen - offSet > max_decrypt_block) {
                cache = cipher.doFinal(data, offSet, max_decrypt_block);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * max_decrypt_block;
        }
        decryptedData = out.toByteArray();
        return decryptedData;
    }
}
