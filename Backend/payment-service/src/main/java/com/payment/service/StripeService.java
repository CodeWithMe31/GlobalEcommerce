
package com.payment.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class StripeService {

  // Replace with your Stripe TEST secret key
  @Value("${stripe.secret.key}")
  private String stripeSecret;

  @PostConstruct
  public void init() {
    Stripe.apiKey = stripeSecret;
  }

  public String createPayment(double amount) throws Exception {
    PaymentIntentCreateParams params =
      PaymentIntentCreateParams.builder()
        .setAmount((long)(amount * 100))
        .setCurrency("usd")
        .setAutomaticPaymentMethods(
          PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
            .setEnabled(true)
            .build()
        )
        .build();

    PaymentIntent intent = PaymentIntent.create(params);
    return intent.getId();
  }

  public void refund(String paymentIntentId) throws Exception {
    RefundCreateParams params =
      RefundCreateParams.builder()
        .setPaymentIntent(paymentIntentId)
        .build();

    Refund.create(params);
  }
}
