
package com.search.repository;

import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.search.model.ProductDocument;

public interface ProductSearchRepository
    extends ElasticsearchRepository<ProductDocument, Long> {

  List<ProductDocument> findByNameContainingIgnoreCase(String name);
}
