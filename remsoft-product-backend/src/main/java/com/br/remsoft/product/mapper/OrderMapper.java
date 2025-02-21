package com.br.remsoft.product.mapper;

import com.br.remsoft.product.dto.request.OrderRequest;
import com.br.remsoft.product.dto.response.OrderResponse;
import com.br.remsoft.product.model.Order;
import com.br.remsoft.product.model.OrderProduct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

import static java.util.Objects.nonNull;

@Component
@AllArgsConstructor
public class OrderMapper {

    private final OrderProductMapper orderProductMapper;

    public OrderResponse toResponse(Order model){
        return OrderResponse.builder()
                .id(model.getId())
                .providerName(model.getProviderName())
                .buyerName(model.getBuyerName())
                .products(nonNull(model.getOrderProducts()) ? model.getOrderProducts()
                        .stream().map(orderProductMapper::toResponse).toList() : new ArrayList<>())
                .totalValue(nonNull(model.getOrderProducts()) ? model.getOrderProducts().stream()
                        .map(p -> p.getValue().multiply(new BigDecimal(p.getAmmout()))).reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO)
                .totalItens(nonNull(model.getOrderProducts()) ? model.getOrderProducts().stream().map(OrderProduct::getAmmout).reduce(0L, Long::sum) : 0L)
                .build();
    }

    public Order toModel(OrderRequest request){
        return Order.builder()
                .providerName(request.getProviderName())
                .buyerName(request.getBuyerName())
                .build();
    }
}
