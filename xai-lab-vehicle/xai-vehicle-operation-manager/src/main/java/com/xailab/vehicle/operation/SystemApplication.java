package com.xailab.vehicle.operation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 运管平台主架构
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages={"com.xailab.*"})
@EnableFeignClients( basePackages = {"com.xailab.vehicle.*"} )
@EnableScheduling
public class SystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SystemApplication.class, args);
	}

}