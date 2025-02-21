package com.br.remsoft.product.service;

import com.br.remsoft.product.dto.request.OrderRequest;
import com.br.remsoft.product.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Page<OrderResponse> list(Pageable pageable);

    OrderResponse findById(Long id);

    OrderResponse create(OrderRequest request);

    OrderResponse update(OrderRequest request, Long id);

    void delete(Long id);
}
