/*
 * @作者: 魏来
 * @日期: 2021/11/17  上午10:21
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework.utils;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * <li>描述: Spring Boot项目升级到JDK 11，运行时发现警告如下：
 * WARNING: An illegal reflective access operation has occurred
 * WARNING: Illegal reflective access by org.springframework.cglib.core.ReflectUtils (file:/D:/Android/.gradle/caches/modules-2/files-2.1/org.springframework/spring-core/5.2.0.RELEASE/e0e1b3c304f70ed19d7905975f6f990916ada219/spring-core-5.2.0.RELEASE.jar) to method java.lang.ClassLoader.defineClass(java.lang.String,byte[],int,int,java.security.ProtectionDomain)
 * WARNING: Please consider reporting this to the maintainers of org.springframework.cglib.core.ReflectUtils
 * WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
 * WARNING: All illegal access operations will be denied in a future release
 */
public class DisableWarning {
    /**
     * <li>: 启动时禁用jdk11 警告
     *
     * @作者: 魏来
     * @日期: 2021/11/17  上午10:23
     **/
    public static void disable() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe u = (Unsafe) theUnsafe.get(null);

            Class<?> cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field logger = cls.getDeclaredField("logger");
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
        } catch (Exception e) {

        }
    }
}
