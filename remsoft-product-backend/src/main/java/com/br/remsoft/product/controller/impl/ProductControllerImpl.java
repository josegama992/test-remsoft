package com.br.remsoft.product.controller.impl;

import com.br.remsoft.product.controller.ProductController;
import com.br.remsoft.product.dto.request.ProductRequest;
import com.br.remsoft.product.dto.response.ProductResponse;
import com.br.remsoft.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RequestMapping("/produto")
@RestController
@AllArgsConstructor
public class ProductControllerImpl implements ProductController {

    private final ProductService service;

    @GetMapping
    @Override
    public ResponseEntity<Page<ProductResponse>> list(Pageable pageable){
        Page<ProductResponse> response = service.list(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ProductResponse> findById(@PathVariable("id") Long id){
        ProductResponse response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Override
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request){
        ProductResponse response = service.create(request);
        return ResponseEntity.created(URI.create(String.format("/produto/%s",response.getId()))).body(response);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<ProductResponse> update(@Valid @RequestBody ProductRequest request, @PathVariable("id") Long id){
        ProductResponse response = service.update(request, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
