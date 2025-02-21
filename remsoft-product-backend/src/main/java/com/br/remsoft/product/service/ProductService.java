package com.br.remsoft.product.service;

import com.br.remsoft.product.dto.request.ProductRequest;
import com.br.remsoft.product.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductResponse> list(Pageable pageable);

    ProductResponse findById(Long id);

    ProductResponse create(ProductRequest request);

    ProductResponse update(ProductRequest request, Long id);

    void delete(Long id);
}
