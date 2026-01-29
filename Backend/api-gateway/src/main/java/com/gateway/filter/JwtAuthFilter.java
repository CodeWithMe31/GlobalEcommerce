
package com.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

@Configuration
public class JwtAuthFilter {

  @Value(${jwt.secret.key})
  private String secret;

  @Bean
  public GlobalFilter jwtFilter() {
    return (exchange, chain) -> {

      String path = exchange.getRequest().getURI().getPath();

      // Public endpoints
      if (path.startsWith("/auth") || path.startsWith("/search")) {
        return chain.filter(exchange);
      }

      String authHeader = exchange.getRequest()
          .getHeaders()
          .getFirst(HttpHeaders.AUTHORIZATION);

      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      }

      try {
        String token = authHeader.substring(7);

        Claims claims = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();

        String user = claims.getSubject();
        String role = claims.get("role", String.class);

        ServerWebExchange modified = exchange.mutate()
            .request(r -> r
                .header("X-USER", user)
                .header("X-ROLE", role))
            .build();

        return chain.filter(modified);

      } catch (Exception e) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
      }
    };
  }
}
