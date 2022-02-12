/**
 *
 * @author weilai
 * @data 2018年11月22日 下午2:43:20
 *
 * @desc 类描述
 *       <li>接受rabbitMQ总线上的消息并持久化
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
public class LogServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LogServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.err.println("********************* 操作日志服务(logServer)启动完成！*********************");
	}

}
