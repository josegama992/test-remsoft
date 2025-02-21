package com.br.remsoft.product.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class OrderProductResponse {

    @JsonProperty("produto")
    @JsonIgnoreProperties("valor")
    private ProductResponse product;

    @JsonProperty("quantidade")
    private Long ammount;

    @JsonProperty("valor")
    private BigDecimal value;

    @JsonProperty("valorTotal")
    private BigDecimal totalValue;
}
