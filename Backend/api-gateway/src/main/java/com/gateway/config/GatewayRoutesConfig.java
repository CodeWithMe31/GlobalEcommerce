
package com.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

  @Bean
  public RouteLocator routes(RouteLocatorBuilder builder) {
    return builder.routes()

      .route("auth-service", r ->
        r.path("/auth/**").uri("http://localhost:8081"))

      .route("product-service", r ->
        r.path("/products/**").uri("http://localhost:8082"))

      .route("cart-service", r ->
        r.path("/cart/**").uri("http://localhost:8083"))

      .route("order-service", r ->
        r.path("/orders/**").uri("http://localhost:8084"))

      .route("payment-service", r ->
        r.path("/payment/**").uri("http://localhost:8085"))

      .route("notification-service", r ->
        r.path("/notify/**").uri("http://localhost:8086"))

      .route("search-service", r ->
        r.path("/search/**").uri("http://localhost:8087"))

      .build();
  }
}
