package com.ogz.reminder;

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
@EnableFeignClients(basePackages = "org.ogz.client")
@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(title = "Reminder-Service", version = "1.0", description = "My-Assistant Reminder Service UI"))
public class MailAssistantReminderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailAssistantReminderServiceApplication.class, args);
	}

}
