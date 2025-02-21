package com.br.remsoft.product.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {

    @JsonProperty("nome")
    @NotBlank
    private String name;
    @JsonProperty("valor")
    @Min(value = 0)
    private BigDecimal value;
}
