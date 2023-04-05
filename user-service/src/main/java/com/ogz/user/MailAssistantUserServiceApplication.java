package com.ogz.user;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
//@OpenAPIDefinition
//@RequestMapping("/api/v1/user-service")
public class MailAssistantUserServiceApplication {

	@Bean
	public OpenAPI customOpenAPI(){
		return new OpenAPI().info(new Info()
				.title("User-Service")
				.description("My-Assistant User Service UI")
				.version("1.0")
				);
	}

	public static void main(String[] args) {
		SpringApplication.run(MailAssistantUserServiceApplication.class, args);
	}

}
