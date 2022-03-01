package com.leesky.ezframework.global;

/**
 * desc：TODO
 *
 * @author： 魏来
 * @date： 2021/12/14 下午12:38
 */
public class Common {


    public final static String USER_ID = "userId";//系统用户id
    public final static String ID_NAME = "idName";//中文名称
    public final static String USER_NAME = "userName";//系统用户登录名称
    public final static String LOGIN_USER_EXT_INFO = "userInfo";//登录用户经常使用的扩展信息


    public final static String CODE = "code";//sms\captcha 方式登录是 请求头部的图片验证码或短信验证码
    public final static String BASIC = "Basic";
    public final static String CLIENT_ID_KEY = "client_id";
    public final static String GRANT_TYPE_KEY = "grant_type";
    public final static String TOKEN_TYPE = "bearer ";//接口认证方式
    public final static String ROLE_LIST = "authorities";//jwt 中用户权限部分
    public final static String URL_HEADER_PARAM = "Authorization";//http请求时头部参数名

    public final static String COMPANY_CODE = "CompanyCode";//公司编码
    public final static String COMPANY_NAME = "CompanyName";//公司名称

    public final static String DEALER_CODE = "dealerCode";//经销商编码
    public final static String DEALER_NAME = "dealerName";//经销商名称


    public final static String CLIENT_PWD_JOIN = "_pwD@?123";//client密码混淆字符(client明文密码是： 用户名+pwD@?123+用户名 或者 mobile+pwD@?123+mobile)

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
    public static String RSA_PUBLIC2048 = "" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwWu/eKYRU0NfKNJ5Dv3ZeYc5PyDMC" +
            "jhYKGerhCo8JYt1aSlVB4GNLYYdSzKvcccn3hi9Zt7p2m0R3643ejyEp1LFBZXHUuafCAbx+E" +
            "pQF1kjzqNBae5v+zao9/He24qd4A53bYhLGR3T+r5WN5ZPYRxdGopXrmon+yTAjsDcc6DR1MM" +
            "yE8+SukeMy5NF8gnwmBbvEuLmWYwzIoOJHOdGDpMumixndErxAK0mrOe8qesQS/qvNFd4MubL" +
            "gU7LamY8XCErxV6WZjxpa4Eit/3uWAtVWcVtDQUT1MOv9VDenEsmNSpd9/Kin5x1dwxYya/kT" +
            "w+VIvtpBTTbdWs32q0FmQIDAQAB";

    //加密长度2048
    //Result.setData方法中 私钥加密
    public static String RSA_PRIVATE2048 = "" +
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

    public static String RSA_PUBLIC1024 = "" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQClbBKcTGYmj9ntGiqYay4lHnQ0w5XHLpYTk" +
            "cGcEnGE6qnhsZE9txhIdJ+rS4ujN4uUE7mzXp4DSL3BH9hYpUmUgWbH+KnB+Dw96vhiubh7KM" +
            "qkPxaXyVFGwBv92k37tSsx+lnX5O25mLdCao4Yxbex2JboajQh7oFtYtd+rInm6QIDAQAB";

    public static String RSA_PRIVATE1024 = "" +
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKVsEpxMZiaP2e0aKphrLiUed" +
            "DTDlcculhORwZwScYTqqeGxkT23GEh0n6tLi6M3i5QTubNengNIvcEf2FilSZSBZsf4qcH4PD" +
            "3q+GK5uHsoyqQ/FpfJUUbAG/3aTfu1KzH6Wdfk7bmYt0JqjhjFt7HYluhqNCHugW1i136sieb" +
            "pAgMBAAECgYBrO0JZJUE2kBtxknu4MApKr5IevgZyhqVsbBi4RlBmHAQhtIx1GCJanZb/YEpV" +
            "dGU7iQng113QvS+caDtDQ2oNGNYWY+zr+pr80EYe0ETP9CT1M2SdSwzTFAVmVWw8y5ehpzGx9" +
            "OsMFoU8eJC60B6SthmoBpC1XPHUzyEcrOlTAQJBAOY7AMWGr+a7xWXcANAsNHokAufiwRjER2" +
            "O14xuipFCsyrArwBXkejDoQ3nJ2BgMtTcFQnCtPNhXu26LZ3JgjMkCQQC38A9ILwhbfpsymqZ" +
            "EqZQzPwe7NclyWCMeqo6YNVP7nyvKan12b8wNtO8IgGeBIkcBV8+EJjWHaD1mmo/LRzkhAkEA" +
            "ziYi6LtTIe0cMbqu17fdPUileXDmqgCGU9f3hTU6oCo4S0rBcljkuIogcRq07cPjN2L0PORBy" +
            "Q21fKRCen3NUQJBAJfqvbRwpAZtWuFAuXKcXLq2pDwLAIZgJSF+3Kb55JM9s31K1rSrQW1Qst" +
            "visZoGJX8Gd2494JglkBDeN0U9XgECQDGjS+sMPXE4xEfBWb1QAxdCh6cCh17v5SwOzD1ewzL" +
            "SPFe9FELI+OK0rtU5e/I0HRxmZe+8i+AysaKDTwUKPQQ=";
}
