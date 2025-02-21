package com.br.remsoft.product.mapper;

import com.br.remsoft.product.dto.request.ProductRequest;
import com.br.remsoft.product.dto.response.ProductResponse;
import com.br.remsoft.product.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product model){
        return ProductResponse.builder()
                .id(model.getId())
                .name(model.getName())
                .value(model.getValue())
                .build();
    }

    public Product toModel(ProductRequest request){
        return Product.builder()
                .name(request.getName())
                .value(request.getValue())
                .build();
    }
}
