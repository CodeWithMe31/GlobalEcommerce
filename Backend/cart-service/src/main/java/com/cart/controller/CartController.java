
package com.cart.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.cart.dto.CartItemRequest;
import com.cart.model.CartItem;
import com.cart.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

  private final CartService service;

  public CartController(CartService service) {
    this.service = service;
  }

  @GetMapping
  public List<CartItem> view(@RequestHeader("X-USER") String user) {
    return service.getCart(user);
  }

  @PostMapping
  public CartItem add(@RequestBody CartItemRequest req,
                      @RequestHeader("X-USER") String user) {
    return service.add(req, user);
  }

  @DeleteMapping("/{id}")
  public void remove(@PathVariable Long id,
                     @RequestHeader("X-USER") String user) {
    service.remove(id, user);
  }
}
