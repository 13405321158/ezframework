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
    public final static String LOGIN_USER_EXT_INFO = "userInfo";//登录用户经常使用的扩展信息


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

    //加密长度2048
    //登录成功后 传递给登录者
    // 作用：1、解密后端传输的加密字符串
    // 作用：2、加密字符串饼并传递后端
    public static String RSA_PUBLIC = "" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwWu/eKYRU0NfKNJ5Dv3ZeYc5PyDMC" +
            "jhYKGerhCo8JYt1aSlVB4GNLYYdSzKvcccn3hi9Zt7p2m0R3643ejyEp1LFBZXHUuafCAbx+E" +
            "pQF1kjzqNBae5v+zao9/He24qd4A53bYhLGR3T+r5WN5ZPYRxdGopXrmon+yTAjsDcc6DR1MM" +
            "yE8+SukeMy5NF8gnwmBbvEuLmWYwzIoOJHOdGDpMumixndErxAK0mrOe8qesQS/qvNFd4MubL" +
            "gU7LamY8XCErxV6WZjxpa4Eit/3uWAtVWcVtDQUT1MOv9VDenEsmNSpd9/Kin5x1dwxYya/kT" +
            "w+VIvtpBTTbdWs32q0FmQIDAQAB";

    //加密长度2048
    //Result.setData方法中 私钥加密
    public static String RSA_PRIVATE = "" +
            "MIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQDBa794phFTQ18o0nkO/dl5h" +
            "zk/IMwKOFgoZ6uEKjwli3VpKVUHgY0thh1LMq9xxyfeGL1m3unabRHfrjd6PISnUsUFlcdS5p" +
            "8IBvH4SlAXWSPOo0Fp7m/7Nqj38d7bip3gDndtiEsZHdP6vlY3lk9hHF0aileuaif7JMCOwNx" +
            "zoNHUwzITz5K6R4zLk0XyCfCYFu8S4uZZjDMig4kc50YOky6aLGd0SvEArSas57yp6xBL+q80" +
            "V3gy5suBTstqZjxcISvFXpZmPGlrgSK3/e5YC1VZxW0NBRPUw6/1UN6cSyY1Kl338qKfnHV3D" +
            "FjJr+RPD5Ui+2kFNNt1azfarQWZAgMBAAECggEBAIYa7bt58OM4//5ux66kRKelIQTq+W1QmZ" +
            "cSIfJV0jP52GMcXC8DHN5mcWMU9l29xp+PqapsfdmK6+yZfy+KTDMzCRWRPoz1B2hqYFRVOei" +
            "mTlRUDuOihRO5VzfqBClVl5akkRtpdyAn9TSRyXdrCQh8wPTI3Hg6dBQF7FhN0ct8jJYtKix/" +
            "VAXHaKa/++fUZyq6Cm3+niUzZJVBAlOTQuN3yajLm+z3xugbR5I8soOCqh5ywOvepz+4tSTre" +
            "rGvV1LdYqujT06ZCm25rOGmMSSLvKi6nBoZs0dA9XgSTKqs+BhH6gLBirzkukAka62SagmPV5" +
            "opYnxyUUYd9EqkZc0CgYEA8zOu86uKfH6YPqtMVxro1SvCyVWBO3JWM46mTSfKaibtYSFRtij" +
            "dDp9od1FkwAXwTWOZj84WPSfrfq+zDwD8PX8VQjE3L9fweKut4oRnA7KObKcLUUcBm+StjKmf" +
            "9T4kt9rJUwWTA7McAuRpkhUlqiq9vzeN4xWEdlQwGi4Q0F8CgYEAy5lva9jJrxvw2Q1cQ9RFb" +
            "GYbkKSxgVNoT38worYcYKRHbCA1Y5rs9U4SjOz2FADWP/3Vh11JudaDXDOVnqWVjSiMZZB98Z" +
            "RC0aa/fTtl0W4yfrLmbnm8p07jIT6nzCC5uFj+OkjuGCnaQ0bWvpqLY9Dw6Jw0IHEp1f3nqIN" +
            "tjQcCgYEA5eMb5C9pt6Z+yPZpMgk4v5AfDutpPkUk4JVd857CQjdLub2iHJth4dmjCO6CWtUC" +
            "QlT8qid3dBWREG6Kxor65DKFaM1Wyj3HjHlT48OeEKuu31vDYe/JwI+X0Vfh4RL96GDCX1zsi" +
            "9m5h65bcH/W25SNSfv8fgQv8x4/Xe/faucCgYEAsoA5UcJlQKMsi//MrqNCooD7FdtuzMXFpD" +
            "0M9PrY8lnVTJUIeeVwbrkeRnPSY0NAlCYFuYIui0HpNUL8oHToIzQzsy/1W4Hy+0BIjpEu+Wi" +
            "12KMcqCsPHVhqhdEa6Dqg2WEcsxkRthmrhGRtDfsMzlx3eljI0tCkLztGiVWBDK0CgYEAs5Jf" +
            "uORtZklSu6naHIy48mPRwstD75+aHCzVAk68GWcoQsOC+o2CVqH/cxJgz0+osQhwNkJDGOvF0" +
            "6um+95vVSJvet4lXwJ5Eho0KpeYppgVMdXAb/pVno+nZb0JSJQmG7lYPOMPIaqzNjOrzAozYb" +
            "Vlor59Hl11ARoMESHhigI=";

}
