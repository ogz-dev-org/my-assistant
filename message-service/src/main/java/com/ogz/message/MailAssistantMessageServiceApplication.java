package com.ogz.message;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "org.ogz.client")
public class MailAssistantMessageServiceApplication {

	@Bean
	public OpenAPI customOpenAPI(){
		return new OpenAPI().info(new Info()
				.title("Message-Service")
				.description("My-Assistant Message Service UI")
				.version("1.0")
		);
	}

	public static void main(String[] args) {
		SpringApplication.run(MailAssistantMessageServiceApplication.class, args);
	}
}
