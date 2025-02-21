package com.br.remsoft.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "pedido")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_comprador", nullable = false)
    private String buyerName;

    @Column(name = "nome_fornecedor", nullable = false)
    private String providerName;

    @OneToMany(mappedBy = "order")
    List<OrderProduct> orderProducts;
}
