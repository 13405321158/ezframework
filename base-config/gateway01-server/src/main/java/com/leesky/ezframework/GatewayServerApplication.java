package com.leesky.ezframework;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayServerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		System.err.println("********************* 网关01服务(Gateway-server01)启动完成！*********************");
	}
}
