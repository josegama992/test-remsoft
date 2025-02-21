package com.br.remsoft.product.mapper;

import com.br.remsoft.product.dto.response.OrderProductResponse;
import com.br.remsoft.product.model.Order;
import com.br.remsoft.product.model.OrderProduct;
import com.br.remsoft.product.model.OrderProductId;
import com.br.remsoft.product.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class OrderProductMapper {

    private final ProductMapper productMapper;

    public OrderProductResponse toResponse(OrderProduct model){
        return OrderProductResponse.builder()
                .value(model.getValue())
                .ammount(model.getAmmout())
                .product(productMapper.toResponse(model.getProduct()))
                .totalValue(model.getValue().multiply(new BigDecimal(model.getAmmout())))
                .build();
    }

    public OrderProduct toModel(Product product, Order order, Long ammount){
        return OrderProduct.builder()
                .id(new OrderProductId(order.getId(), product.getId()))
                .order(order)
                .product(product)
                .value(product.getValue())
                .ammout(ammount)
                .build();

    }

    public List<OrderProduct> toModel(List<Product> products, Order order, Map<Long, Long> productValueMap){
        return products.stream().map(p -> toModel(p, order, productValueMap.get(p.getId()))).toList();
    }

}
