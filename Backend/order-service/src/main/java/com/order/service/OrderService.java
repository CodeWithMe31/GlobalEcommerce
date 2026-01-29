
package com.order.service;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.order.dto.*;
import com.order.model.*;
import com.order.repository.OrderRepository;

@Service
public class OrderService {

  private final OrderRepository repo;
  private final InvoiceService invoiceService;
  private final RestTemplate rest = new RestTemplate();

  private final String CART_URL = "http://localhost:8083/cart";
  private final String REFUND_URL = "http://localhost:8085/payment/refund/";
  private final String EMAIL_URL = "http://localhost:8086/notify";

  public OrderService(OrderRepository repo, InvoiceService invoiceService) {
    this.repo = repo;
    this.invoiceService = invoiceService;
  }

  public Order place(String user, PlaceOrderRequest req) {
    CartItemDTO[] cart = rest.getForObject(CART_URL, CartItemDTO[].class);
    if (cart == null || cart.length == 0) {
      throw new RuntimeException("Cart empty");
    }

    List<OrderItem> items = new ArrayList<>();
    double total = 0;

    for (CartItemDTO c : cart) {
      OrderItem i = new OrderItem();
      i.setProductId(c.productId);
      i.setProductName(c.productName);
      i.setPrice(c.price);
      i.setQuantity(c.quantity);
      total += c.price * c.quantity;
      items.add(i);
    }

    Order o = new Order();
    o.setUserEmail(user);
    o.setStatus("PLACED");
    o.setShippingAddress(req.shippingAddress);
    o.setPaymentIntentId(req.paymentIntentId);
    o.setItems(items);
    o.setTotalAmount(total);

    repo.save(o);
    rest.delete(CART_URL);
    return o;
  }

  public List<Order> history(String user) {
    return repo.findByUserEmail(user);
  }

  public void cancel(Long id, String user) {
    Order o = repo.findById(id).orElseThrow();
    if (!o.getUserEmail().equals(user)) throw new RuntimeException("Forbidden");
    rest.postForObject(REFUND_URL + o.getPaymentIntentId(), null, Void.class);
    o.setStatus("REFUNDED");
    repo.save(o);
  }

  public byte[] invoice(Long id, String user) throws Exception {
    Order o = repo.findById(id).orElseThrow();
    if (!o.getUserEmail().equals(user)) throw new RuntimeException("Forbidden");
    return invoiceService.generate(o);
  }
}
