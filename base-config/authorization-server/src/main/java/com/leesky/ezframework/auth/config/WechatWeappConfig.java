package com.leesky.ezframework.auth.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信小程序类应用配置，使用weixin-java-miniapp 开源jar
 * appid、secret 是在微信开放平台注册开发者帐号，并拥有一个已审核通过的【移动应用】获取的
 */
@Configuration
public class WechatWeappConfig {

    //微信小程序
    @Value("${wx.miniapp.appid}")
    private String appid01;
    @Value("${wx.miniapp.secret}")
    private String secret01;


    //公众号
    @Value("${wx.mp.appid}")
    private String appid02;
    @Value("${wx.mp.secret}")
    private String secret02;


    //微信小程序服务接口： 程序中注入这个bean
    @Bean
    public WxMaService wxMaService(WxMaConfig wxMaConfig) {
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(wxMaConfig);
        return service;
    }

    /**
     * <li>微信公众号服务接口：程序中 注入这个bean即可</li>
     *
     * @author: 魏来
     * @date: 2022/3/7 下午3:35
     */
    @Bean
    public WxMpService wxMpService(WxMpConfigStorage wxMpConfigStorage) {
        WxMpServiceImpl service = new WxMpServiceImpl();
        service.setWxMpConfigStorage(wxMpConfigStorage);
        return service;
    }

    /**
     * <li>微信小程序配置</li>
     *
     * @author: 魏来
     * @date: 2022/3/7 下午3:37
     */
    @Bean
    public WxMaConfig wxMaConfig() {
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(appid01);
        config.setSecret(secret01);
        return config;
    }

    /**
     * <li>微信公众号配置</li>
     *
     * @author: 魏来
     * @date: 2022/3/7 下午3:28
     */
    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(appid02);
        config.setSecret(secret02);

        return config;
    }

}
