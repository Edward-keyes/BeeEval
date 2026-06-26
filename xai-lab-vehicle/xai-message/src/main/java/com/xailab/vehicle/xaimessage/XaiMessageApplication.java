package com.xailab.vehicle.xaimessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class XaiMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(XaiMessageApplication.class, args);
    }

}
