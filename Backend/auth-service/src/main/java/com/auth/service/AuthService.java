
package com.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.auth.dto.LoginRequest;
import com.auth.dto.RegisterRequest;
import com.auth.model.User;
import com.auth.repository.UserRepository;
import com.auth.security.JwtUtil;

@Service
public class AuthService {

  private final UserRepository repo;
  private final BCryptPasswordEncoder encoder;

  public AuthService(UserRepository repo, BCryptPasswordEncoder encoder) {
    this.repo = repo;
    this.encoder = encoder;
  }

  public void register(RegisterRequest req) {
    if (repo.findByEmail(req.email).isPresent()) {
      throw new RuntimeException("User already exists");
    }

    User u = new User();
    u.setEmail(req.email);
    u.setPassword(encoder.encode(req.password));
    u.setRole(req.role == null ? "USER" : req.role);

    repo.save(u);
  }

  public String login(LoginRequest req) {
    User u = repo.findByEmail(req.email)
      .orElseThrow(() -> new RuntimeException("Invalid credentials"));

    if (!encoder.matches(req.password, u.getPassword())) {
      throw new RuntimeException("Invalid credentials");
    }

    return JwtUtil.generateToken(u.getEmail(), u.getRole());
  }
}
