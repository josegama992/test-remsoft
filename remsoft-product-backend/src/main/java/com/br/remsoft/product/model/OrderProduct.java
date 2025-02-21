package com.br.remsoft.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "pedido_produto")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {

    @EmbeddedId
    private OrderProductId id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "pedido_id")
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "produto_id")
    private Product product;

    @Column(name = "quantidade", nullable = false)
    private Long ammout;

    @Column(name = "valor_unitario", nullable = false)
    private BigDecimal value;
}
