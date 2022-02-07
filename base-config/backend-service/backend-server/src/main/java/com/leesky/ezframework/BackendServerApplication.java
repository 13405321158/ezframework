/*
  @author:weilai
 * @Data:2020年4月29日下午6:15:54
 * @Org:Sentury Co.,ltd.
 * @Department:Domestic Sales,Tech Center
 * @Desc:
 *        <li>管控平台后端支撑服务
 */

package com.leesky.ezframework;

import com.leesky.ezframework.utils.DisableWarning;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;



@EnableDiscoveryClient
@SpringBootApplication
public class BackendServerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		DisableWarning.disable(); //启动时禁用jdk11 警告
		SpringApplication.run(BackendServerApplication.class, args);

	}

	@Override
	public void run(String... args)  {

		System.err.println("********************* 后台支撑服务(BackendServer)启动完成！*********************");
	}

}
