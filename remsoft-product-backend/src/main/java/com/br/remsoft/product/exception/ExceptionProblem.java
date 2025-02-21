package com.br.remsoft.product.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionProblem {

    @Schema(example = "400")
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private String title;
    private String detail;
    private String userMessage;
    private List<Detail> invalidFields;

    @Getter
    @Setter
    public static class Detail {
        private String field;
        private String message;
    }

}