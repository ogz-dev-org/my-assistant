package com.ogz.apigateway;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.ogz.model.User;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info = @Info(title = "API Gateway", version = "1.0", description = "Documentation API Gateway v1.0"))
public class ApiGatewayApplication {

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder
				.routes()
				.route(r -> r.path("/user-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://user-service"))
				.route(r -> r.path("/mail-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://mail-service"))
				.route(r -> r.path("/reminder-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://reminder" +
						"-service"))
				.route(r -> r.path("/message-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://message" +
						"-service"))
				.route(r -> r.path("/web-rtc-facetime-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb" +
						"://web-rtc-facetime-service"))

    .build();
	}

//	@Bean
//	public OpenAPI customOpenAPI(){
//		return new OpenAPI().info(new Info()
//				.title("Gateway-Service")
//				.description("My-Assistant Gateway Service UI")
//				.version("1.0")
//		);
//	}


//	@Bean
//	@Lazy(false)
//	public List<GroupedOpenApi> apis(RouteDefinitionLocator locator) {
//		List<GroupedOpenApi> groups = new ArrayList<>();
//		List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
//		for (RouteDefinition definition : definitions) {
//			System.out.println("id: " + definition.getId() + "  " + definition.getUri().toString());
//		}
//		definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
//
//			String name = routeDefinition.getId().replaceAll("-service", "");
//			groups.add(GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build());
//		});
//		System.out.println(groups);
//		return groups;
//	}

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
