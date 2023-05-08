package com.ogz.message;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.ogz.constants.Secrets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "org.ogz.client")
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
		System.out.println(Secrets.CLIENT_SECRET_FILE);
		SpringApplication.run(MailAssistantUserServiceApplication.class, args);
	}

}
