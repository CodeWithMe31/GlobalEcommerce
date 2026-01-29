
package com.notify.controller;

import org.springframework.web.bind.annotation.*;
import com.notify.service.EmailService;

@RestController
@RequestMapping("/notify")
public class EmailController {

  private final EmailService service;

  public EmailController(EmailService service) {
    this.service = service;
  }

  @PostMapping
  public void notify(@RequestParam String email,
                     @RequestParam String subject,
                     @RequestParam String message) {
    service.send(email, subject, message);
  }
}
