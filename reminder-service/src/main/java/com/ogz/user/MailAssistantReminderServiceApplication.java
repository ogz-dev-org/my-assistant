package com.ogz.user;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients
public class MailAssistantReminderServiceApplication {

	@Bean
	public OpenAPI customOpenAPI(){
		return new OpenAPI().info(new Info()
				.title("Reminder-Service")
				.description("My-Assistant Reminder Service UI")
				.version("1.0")
		);
	}

	public static void main(String[] args) {
		SpringApplication.run(MailAssistantReminderServiceApplication.class, args);
	}

}
