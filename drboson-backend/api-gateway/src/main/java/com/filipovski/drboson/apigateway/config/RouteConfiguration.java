package com.filipovski.drboson.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/auth/**")
                    .and()
                    .uri("lb://auth-service"))
                .route(r -> r.path("/user/**")
                    .and()
                    .uri("lb://users-service"))
                .route(r -> r.path("/project/**")
                    .and()
                    .uri("lb://projects-service"))
                .route(r -> r.path("/dataset/**")
                    .and()
                    .uri("lb://datasets-service"))
                .route(r -> r.path("/run/**")
                    .and()
                    .uri("lb://runs-service"))
                .build();
    }
}
