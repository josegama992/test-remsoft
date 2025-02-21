package com.br.remsoft.product.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("nomeComprador")
    private String buyerName;

    @JsonProperty("nomeForncedor")
    private String providerName;

    @JsonProperty("itensTotais")
    private Long totalItens;

    @JsonProperty("valorTotal")
    private BigDecimal totalValue;

    @JsonProperty("produtos")
    List<OrderProductResponse> products;
}
