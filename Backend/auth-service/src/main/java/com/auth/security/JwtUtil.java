
package com.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtUtil {

  private static final String SECRET = "CHANGE_THIS_SECRET";
  private static final long EXPIRY = 1000 * 60 * 60 * 24;

  public static String generateToken(String email, String role) {
    return Jwts.builder()
      .setSubject(email)
      .claim("role", role)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + EXPIRY))
      .signWith(SignatureAlgorithm.HS256, SECRET)
      .compact();
  }
}
