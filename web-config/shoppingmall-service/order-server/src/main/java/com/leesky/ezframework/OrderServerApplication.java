/*
 * @作者: 魏来
 * @日期: 2021/9/16  上午11:36
 * @组织: 森麒麟轮胎股份有限公司.
 * @部门: 国内市场替换部IT组
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
public class OrderServerApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(OrderServerApplication.class, args);

    }

    @Override
    public void run(String... args)  {

        System.err.println("********************* 商城订单服务(OrderServer)启动完成！*********************");
    }


}
