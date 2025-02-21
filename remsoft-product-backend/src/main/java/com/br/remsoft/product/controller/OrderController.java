package com.br.remsoft.product.controller;

import com.br.remsoft.product.dto.request.OrderRequest;
import com.br.remsoft.product.dto.response.OrderResponse;
import com.br.remsoft.product.exception.ExceptionProblem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderController {

    @Operation(summary = "Busca todos os pedidos Paginado.", method = "GET")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponse.class)) }, description = "Requisição com sucesso"),
            @ApiResponse(responseCode = "404", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionProblem.class)) }, description = "O recurso não foi encontrado") })
    ResponseEntity<Page<OrderResponse>> list(@ParameterObject Pageable pageable);

    @Operation(summary = "Busca pedido por id.", method = "GET")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponse.class)) }, description = "Requisição com sucesso"),
            @ApiResponse(responseCode = "404", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionProblem.class)) }, description = "O recurso não foi encontrado") })
    ResponseEntity<OrderResponse> findById(@PathVariable("id") Long id);

    @Operation(summary = "Cria um pedido.", method = "POST")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponse.class)) }, description = "Requisição com sucesso"),
            @ApiResponse(responseCode = "404", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionProblem.class)) }, description = "O recurso não foi encontrado") })
    ResponseEntity<OrderResponse> create(@RequestBody OrderRequest request);

    @Operation(summary = "Atualiza um pedido por id.", method = "PUT")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponse.class)) }, description = "Requisição com sucesso"),
            @ApiResponse(responseCode = "404", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionProblem.class)) }, description = "O recurso não foi encontrado") })
    ResponseEntity<OrderResponse> update(@RequestBody OrderRequest request, @PathVariable("id") Long id);

    @Operation(summary = "Deleta um pedido por id.", method = "DELETE")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderResponse.class)) }, description = "Requisição com sucesso"),
            @ApiResponse(responseCode = "404", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionProblem.class)) }, description = "O recurso não foi encontrado") })
    ResponseEntity<Void> delete(@PathVariable("id") Long id);
}
