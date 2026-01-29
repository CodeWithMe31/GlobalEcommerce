
package com.payment.controller;

import org.springframework.web.bind.annotation.*;
import com.payment.service.StripeService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

  private final StripeService stripeService;

  public PaymentController(StripeService stripeService) {
    this.stripeService = stripeService;
  }

  @PostMapping("/create")
  public String create(@RequestParam double amount) throws Exception {
    return stripeService.createPayment(amount);
  }

  @PostMapping("/refund/{paymentIntentId}")
  public void refund(@PathVariable String paymentIntentId) throws Exception {
    stripeService.refund(paymentIntentId);
  }
}
