
package com.order.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.order.dto.PlaceOrderRequest;
import com.order.model.Order;
import com.order.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

  private final OrderService service;

  public OrderController(OrderService service) {
    this.service = service;
  }

  @PostMapping
  public Order place(@RequestHeader("X-USER") String user,
                     @RequestBody PlaceOrderRequest req) {
    return service.place(user, req);
  }

  @GetMapping
  public List<Order> history(@RequestHeader("X-USER") String user) {
    return service.history(user);
  }

  @PostMapping("/{id}/cancel")
  public void cancel(@PathVariable Long id,
                     @RequestHeader("X-USER") String user) {
    service.cancel(id, user);
  }

  @GetMapping("/{id}/invoice")
  public ResponseEntity<byte[]> invoice(@PathVariable Long id,
                                        @RequestHeader("X-USER") String user)
      throws Exception {
    byte[] pdf = service.invoice(id, user);
    return ResponseEntity.ok()
      .header("Content-Disposition", "attachment; filename=invoice_" + id + ".pdf")
      .body(pdf);
  }
}
