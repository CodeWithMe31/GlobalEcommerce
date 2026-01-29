
package com.order.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
  List<Order> findByUserEmail(String userEmail);
}
