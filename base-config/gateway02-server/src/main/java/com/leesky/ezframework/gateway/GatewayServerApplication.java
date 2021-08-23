package com.leesky.ezframework.gateway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayServerApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		System.err.println("********************* 网关02服务(Gateway-server02)启动完成！*********************");
	}
}
