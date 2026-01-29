
package com.product.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.product.dto.ProductRequest;
import com.product.model.Product;
import com.product.repository.ProductRepository;

@Service
public class ProductService {

  private final ProductRepository repo;
  private final RestTemplate restTemplate = new RestTemplate();
  private final String SEARCH_SERVICE_URL = "http://localhost:8087/search/index";

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
    Product saved = repo.save(p);

    // Sync to search service
    try {
      restTemplate.postForObject("http://localhost:8087/search/index", saved, Void.class);
    } catch (Exception e) {
      System.err.println("Search indexing failed: " + e.getMessage());
    }
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
    Product updated = repo.save(p);

    // Ensure the updated product is also reflected in search
    restTemplate.postForObject(SEARCH_SERVICE_URL, updated, Void.class);
    
    return updated;
  }

  public void delete(Long id) {
    repo.deleteById(id);
  }
}
