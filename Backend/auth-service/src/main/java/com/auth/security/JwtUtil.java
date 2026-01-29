
package com.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {

  @Value(${jwt.secret.key})
  private String secret;
  private static final long EXPIRY = 1000 * 60 * 60 * 24;

  public String generateToken(String email, String role) {
    return Jwts.builder()
      .setSubject(email)
      .claim("role", role)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + EXPIRY))
      .signWith(SignatureAlgorithm.HS256, secret)
      .compact();
  }
}
