package com.ogz.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@RequestMapping("/api/v1/user-service")
public class MailAssistantUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailAssistantUserServiceApplication.class, args);
	}

}
