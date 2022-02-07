/*
 * @author:weilai
 * @Data:2020年9月30日下午7:06:50
 * @Org:Sentury Co.,ltd.
 * @Department:Domestic Sales,Tech Center
 */

package com.leesky.ezframework;

import com.leesky.ezframework.nosql.dao.Impl.BaseDaoImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableDiscoveryClient
@SpringBootApplication
@EnableMongoRepositories(repositoryBaseClass = BaseDaoImpl.class)
public class ToolsServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ToolsServiceApplication.class, args);
	}

	@Override
	public void run(String... args)  {
		System.err.println("********************* 平台工具服务(ToolsServer)启动完成！*********************");
	}

}
