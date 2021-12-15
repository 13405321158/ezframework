package com.leesky.ezframework.global;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/14 下午12:38
 */
public class Common {

    public final static String USER_ID = "userId";//系统用户id
    public final static String USER_NAME = "userName";//系统用户登录名称
    public final static String LOGIN_USER_EXT_INFO="userInfo";//登录用户经常使用的扩展信息


    public final static String TOKEN_TYPE = "bearer ";//接口认证方式
    public final static String ROLE_LIST = "authorities";//jwt 中用户权限部分
    public final static String URL_HEADER_PARAM = "Authorization";//http请求时头部参数名




    /**
     * 静态资源
     */
    public static final String[] STATIC_RESOURCES = new String[]{
            "/**/*.ico",
            "/**/*.html",
            "/**/*.png",
            "/**/*.jpg",
            "/**/*.gif",
            "/**/*.css",
            "/**/*.js"
    };
    /**
     * 不需要token访问路径
     */
    public static final String[] WHITE_LIST = new String[]{"/**/public", "/stomp/**", "/v3/api-docs", "/swagger-resources", "/error"};


    public static String RSA_PUBLIC = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjs6szHA2vWPv17BY1YqWYnVls1ny92GGXpQbaitFU0EDlgpxL+x4HG3z9ygPjX9rETh5u81kjQriDIdJ77r2MsSxOGjXtoFUAYzwvyWEalijW3jhb1RD+H4xnnmXfm9SeAalhVprBrfWALAmWwD24fw8BZ2fsXvVfOp9OdNruIZ2We/VXa+o6sNpLhkR8NOZgCq4/0N1JC+D6LptBNqbdTqNVTjscE4Swg8mCo9otujaZMbzG2iTEXs8ZMcadeU63JjkpohodqwaKeoIYhv0csBCI+hOvIyFBUItKuHZ9LewtROeSqLrha+f+SoCgMkomxfjsrUzKtuzFqR1V7+A2wIDAQAB";

    public static String RSA_PRIVATE = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCOzqzMcDa9Y+/XsFjVipZidWWzWfL3YYZelBtqK0VTQQOWCnEv7HgcbfP3KA+Nf2sROHm7zWSNCuIMh0nvuvYyxLE4aNe2gVQBjPC/JYRqWKNbeOFvVEP4fjGeeZd+b1J4BqWFWmsGt9YAsCZbAPbh/DwFnZ+xe9V86n0502u4hnZZ79Vdr6jqw2kuGRHw05mAKrj/Q3UkL4Poum0E2pt1Oo1VOOxwThLCDyYKj2i26NpkxvMbaJMRezxkxxp15TrcmOSmiGh2rBop6ghiG/RywEIj6E68jIUFQi0q4dn0t7C1E55KouuFr5/5KgKAySibF+OytTMq27MWpHVXv4DbAgMBAAECggEAVzjsWcF/7ZUIfJh86ZBH4cMKZszdlTK/uyyCwgnDweHHqvCdyUOI6PRPOfoB5c6ZOl9gGtCMRYkMH9LR2dY7YS65f1apyhKbmIbY1Ca6Bao1vDzMlA7HpRKOg9iIKvUbTavhlN1w2R2XW7w68QJZyBUjI9ruGp0j+7PM4HC9Lmof/a+IzhRwRqq2JWNn1yFEab1vR5YiPoYSLKV7v9iN4W/QRfzu3Gy3Q5MxM8w4qXvu5xlXGqtfL6fy55oDj+unJQ05nZxB1SzTiLhTP8YP395Y71aICG598rkshBu1c/uZTZQ9InVYGiIO31jpuTO5/yCN4gMuXEv4F5rTCRnfwQKBgQD6GbFcS7xHOs9l1KKeozfhY7OAXuy5xxeMY6svghUyb4D3bXw5Qp8mQUq8yusyKx8BO/ygdTluuZ/hh7eGZG1iftLRQGwZ89itvapPBHSnU59mMugIdg3DGf5Ap7zE8YllLc38moAZJM8ancxbusBg1z1sI1s6/ZnQTU5YksPUyQKBgQCSLQ98/9sxAqQW5L9M42Jkg+hqWfQTXKGRiDvg5Shtb3Hkw2cfVLLdj6p3Rifhk7cRjGfKyFiStebz7I7HJK9w6GaWGvA7eANiqorOxwSM3B1ZaSngATpwJp8T5oej0EqUom4pb1in3UTY8z+CR6zRXZxsixKKtt/GrKpwjSGugwKBgCzzXzhKeG2I+WvA+vVuOoXtCH8jEIMXcHkQFN12EN8+wTw3VmNoQPh8NbWW9wzOSUVMhcxFJWxsOnsrjGXAORTB3Jw1aviDJkeb1jdbgqfsVLLmpWOX7ENLsbHnM/tluh8QS3yCPFHIltyezfMSrq75IJYGCMiiefnBWzp6Lm5ZAoGAFpptKLEPndaAtSGOCz+l+VEEYV2jhlLukOh2CeKli7aN8AffLFMWjXgnreXlSNZkmowRiKPOPnFuUw2jTlv33QeyxUdJGokGTP1+FVdk9rsatraySPcnqCvwsU0WcFhHEBDF5BZZqStn9/w1N9oJeR52j7o5lcWNeNU+TcUlDi0CgYASTQAfsg8fLYUVcXNEXANC8QcZhbpAes+tBakP4xTLru7TR3SHF0344YjjWFyrLnOUvlgnUmZkeBhHFniYTUXscZJlylcYJMyboj//tuVO9mHZ5Dv+MY8Jn5KqH/CAxzgjJt7QB4dqJcYW2WYxomsMOBnSma6aW2g4K7u4/hQ2aA==";

}
