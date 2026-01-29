
package com.auth.controller;

import org.springframework.web.bind.annotation.*;

import com.auth.dto.*;
import com.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthService service;

  public AuthController(AuthService service) {
    this.service = service;
  }

  @PostMapping("/register")
  public void register(@RequestBody RegisterRequest req) {
    service.register(req);
  }

  @PostMapping("/login")
  public AuthResponse login(@RequestBody LoginRequest req) {
    return new AuthResponse(service.login(req));
  }
}
