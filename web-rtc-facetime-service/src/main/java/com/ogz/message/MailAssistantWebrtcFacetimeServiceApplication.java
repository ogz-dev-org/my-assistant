package com.ogz.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableFeignClients(basePackages = "org.ogz.client")
public class MailAssistantWebrtcFacetimeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailAssistantWebrtcFacetimeServiceApplication.class, args);
	}

}
