/*
  @author:weilai
 * @Data:2020年4月29日下午6:15:54
 * @Org:Sentury Co.,ltd.
 * @Department:Domestic Sales,Tech Center
 * @Desc:
 *        <li>管控平台后端支撑服务
 */

package com.leesky.ezframework;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ElasticsearchServiceApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchServiceApplication.class, args);
    }

    @Override
    public void run(String... args)  {
        System.err.println("********************* Elasticsearch服务(logServer)启动完成！*********************");
    }

}
