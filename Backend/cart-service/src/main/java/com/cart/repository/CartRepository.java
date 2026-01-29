
package com.cart.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cart.model.CartItem;

public interface CartRepository extends JpaRepository<CartItem, Long> {
  List<CartItem> findByUserEmail(String userEmail);
}
