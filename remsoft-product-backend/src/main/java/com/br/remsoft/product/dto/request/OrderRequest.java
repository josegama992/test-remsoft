package com.br.remsoft.product.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class OrderRequest {

    @JsonProperty("nomeComprador")
    private String buyerName;

    @JsonProperty("nomeForncedor")
    private String providerName;

    @JsonProperty("produtos")
    private List<OrderProductRequest> orderProduct;
}
