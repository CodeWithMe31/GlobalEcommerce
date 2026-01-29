
package com.product.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.product.dto.ProductRequest;
import com.product.model.Product;
import com.product.repository.ProductRepository;

@Service
public class ProductService {

  private final ProductRepository repo;

  public ProductService(ProductRepository repo) {
    this.repo = repo;
  }

  public List<Product> getAll() {
    return repo.findAll();
  }

  public Product create(ProductRequest req) {
    Product p = new Product();
    p.setName(req.name);
    p.setDescription(req.description);
    p.setPrice(req.price);
    p.setStock(req.stock);
    p.setCategory(req.category);
    return repo.save(p);
    Product saved = repo.save(p);

    // Sync to search service
    restTemplate.postForObject("http://localhost:8087/search/index", saved, Void.class);
    return saved;
  }

  public Product update(Long id, ProductRequest req) {
    Product p = repo.findById(id)
      .orElseThrow(() -> new RuntimeException("Product not found"));
    p.setName(req.name);
    p.setDescription(req.description);
    p.setPrice(req.price);
    p.setStock(req.stock);
    p.setCategory(req.category);
    return repo.save(p);
  }

  public void delete(Long id) {
    repo.deleteById(id);
  }
}
