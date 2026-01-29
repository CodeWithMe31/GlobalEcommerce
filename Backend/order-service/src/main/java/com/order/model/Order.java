
package com.order.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String userEmail;
  private String status;
  private double totalAmount;
  private String shippingAddress;
  private String paymentIntentId;

  @OneToMany(cascade = CascadeType.ALL)
  private List<OrderItem> items;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getUserEmail() { return userEmail; }
  public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public double getTotalAmount() { return totalAmount; }
  public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

  public String getShippingAddress() { return shippingAddress; }
  public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

  public String getPaymentIntentId() { return paymentIntentId; }
  public void setPaymentIntentId(String paymentIntentId) { this.paymentIntentId = paymentIntentId; }

  public List<OrderItem> getItems() { return items; }
  public void setItems(List<OrderItem> items) { this.items = items; }
}
