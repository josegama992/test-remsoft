package com.br.remsoft.product.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderProductRequest {

    @JsonProperty("produtoId")
    private Long productId;

    @JsonProperty("quantidade")
    private Long ammount;

}
