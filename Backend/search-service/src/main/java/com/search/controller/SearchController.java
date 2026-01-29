
package com.search.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.search.model.ProductDocument;
import com.search.service.SearchService;

@RestController
@RequestMapping("/search")
public class SearchController {

  private final SearchService service;

  public SearchController(SearchService service) {
    this.service = service;
  }

  @PostMapping("/index")
  public void index(@RequestBody ProductDocument doc) {
    service.index(doc);
  }

  @GetMapping
  public List<ProductDocument> search(@RequestParam String q) {
    return service.search(q);
  }
}
