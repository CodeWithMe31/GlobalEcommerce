
package com.product.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.product.dto.ProductRequest;
import com.product.model.Product;
import com.product.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }

  @GetMapping
  public List<Product> list() {
    return service.getAll();
  }

  @PostMapping
  public Product add(@RequestBody ProductRequest req,
                     @RequestHeader("X-ROLE") String role) {
    if (!"ADMIN".equals(role)) {
      throw new RuntimeException("Forbidden");
    }
    return service.create(req);
  }

  @PutMapping("/{id}")
  public Product update(@PathVariable Long id,
                        @RequestBody ProductRequest req,
                        @RequestHeader("X-ROLE") String role) {
    if (!"ADMIN".equals(role)) {
      throw new RuntimeException("Forbidden");
    }
    return service.update(id, req);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id,
                     @RequestHeader("X-ROLE") String role) {
    if (!"ADMIN".equals(role)) {
      throw new RuntimeException("Forbidden");
    }
    service.delete(id);
  }
}
