
package com.payment.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

  // Replace with your Stripe TEST secret key
  private static final String STRIPE_SECRET = "sk_test_CHANGE_ME";

  static {
    Stripe.apiKey = STRIPE_SECRET;
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
