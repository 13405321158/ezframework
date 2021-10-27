/*
 * @作者: 魏来
 * @日期: 2021/10/23  上午10:54
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
 * @描述:
 */
package com.leesky.ezframework;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <li>描述:
 */
@EnableDiscoveryClient
@SpringBootApplication
public class CartServerApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(CartServerApplication.class, args);

    }

    @Override
    public void run(String... args)  {

        System.err.println("********************* 商城购物车服务(OrderServer)启动完成！*********************");
    }
}
