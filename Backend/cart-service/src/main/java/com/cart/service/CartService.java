
package com.cart.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.cart.dto.CartItemRequest;
import com.cart.model.CartItem;
import com.cart.repository.CartRepository;

@Service
public class CartService {

  private final CartRepository repo;

  public CartService(CartRepository repo) {
    this.repo = repo;
  }

  public List<CartItem> getCart(String user) {
    return repo.findByUserEmail(user);
  }

  public CartItem add(CartItemRequest req, String user) {
    CartItem item = new CartItem();
    item.setUserEmail(user);
    item.setProductId(req.productId);
    item.setProductName(req.productName);
    item.setPrice(req.price);
    item.setQuantity(req.quantity);
    return repo.save(item);
  }

  public void remove(Long id, String user) {
    CartItem item = repo.findById(id)
      .orElseThrow(() -> new RuntimeException("Item not found"));
    if (!item.getUserEmail().equals(user)) {
      throw new RuntimeException("Forbidden");
    }
    repo.deleteById(id);
  }

  public void clear(String user) {
    repo.findByUserEmail(user).forEach(repo::delete);
  }
}
