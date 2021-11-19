/*
 * @作者: 魏来
 * @日期: 2021/11/16  下午6:58
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework;

import com.leesky.ezframework.utils.DisableWarning;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <li>描述：认证服务器
 * <li>Spring Authorization Server 是 Spring 团队最新开发适配 OAuth 协议的授权服务器项目，当前版本是0.2(可以运用到生产环境)
 * <li>由于官方还未提供对应的 Spring Boot Starter 自动化配置，需要自己配置相关的bean,同时服务还不支持用户名和密码方式(grant_type = password 的认证方式)，
 * <li>所以授权服务器暂时使用 spring-cloud-starter-oauth2等待 spring-authorization-server 成熟再切换
 */
@EnableDiscoveryClient
@SpringBootApplication
public class AuthorizationServerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        DisableWarning.disable(); //启动时禁用jdk11 警告
        SpringApplication.run(AuthorizationServerApplication.class, args);

    }

    @Override
    public void run(String... args) {

        System.err.println("********************* 平台认证服务器(AuthorizationServer)启动完成！*********************");
    }


}
