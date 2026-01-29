
package com.search.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.search.model.ProductDocument;
import com.search.repository.ProductSearchRepository;

@Service
public class SearchService {

  private final ProductSearchRepository repo;

  public SearchService(ProductSearchRepository repo) {
    this.repo = repo;
  }

  public void index(ProductDocument doc) {
    repo.save(doc);
  }

  public List<ProductDocument> search(String keyword) {
    return repo.findByNameContainingIgnoreCase(keyword);
  }
}
