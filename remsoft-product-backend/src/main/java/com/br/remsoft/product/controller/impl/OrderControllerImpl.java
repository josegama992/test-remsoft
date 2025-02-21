package com.br.remsoft.product.controller.impl;

import com.br.remsoft.product.controller.OrderController;
import com.br.remsoft.product.dto.request.OrderRequest;
import com.br.remsoft.product.dto.response.OrderResponse;
import com.br.remsoft.product.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/pedido")
@RestController
@AllArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService service;

    @GetMapping
    @Override
    public ResponseEntity<Page<OrderResponse>> list(Pageable pageable){
        Page<OrderResponse> response = service.list(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<OrderResponse> findById(@PathVariable("id") Long id){
        OrderResponse response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Override
    public ResponseEntity<OrderResponse> create(@RequestBody OrderRequest request) {
        OrderResponse response = service.create(request);
        return ResponseEntity.created(URI.create(String.format("/pedido/%s",response.getId()))).body(response);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<OrderResponse> update(@RequestBody OrderRequest request, @PathVariable("id") Long id) {
        OrderResponse response = service.update(request, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
