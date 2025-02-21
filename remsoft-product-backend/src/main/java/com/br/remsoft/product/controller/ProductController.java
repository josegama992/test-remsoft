package com.br.remsoft.product.controller;

import com.br.remsoft.product.dto.request.ProductRequest;
import com.br.remsoft.product.dto.response.ProductResponse;
import com.br.remsoft.product.exception.ExceptionProblem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


public interface ProductController {

    @Operation(summary = "Busca todos os produtos Paginado.", method = "GET")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponse.class)) }, description = "Requisição com sucesso"),
            @ApiResponse(responseCode = "404", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionProblem.class)) }, description = "O recurso não foi encontrado") })
    ResponseEntity<Page<ProductResponse>> list(@ParameterObject Pageable pageable);

    @Operation(summary = "Busca Produto por id.", method = "GET")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponse.class)) }, description = "Requisição com sucesso"),
            @ApiResponse(responseCode = "404", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionProblem.class)) }, description = "O recurso não foi encontrado") })
    ResponseEntity<ProductResponse> findById(@PathVariable("id") Long id);

    @Operation(summary = "Cria um produto.", method = "POST")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponse.class)) }, description = "Requisição com sucesso"),
            @ApiResponse(responseCode = "404", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionProblem.class)) }, description = "O recurso não foi encontrado") })
    ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request);

    @Operation(summary = "Atualiza um produto pelo id.", method = "PUT")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponse.class)) }, description = "Requisição com sucesso"),
            @ApiResponse(responseCode = "404", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionProblem.class)) }, description = "O recurso não foi encontrado") })
    ResponseEntity<ProductResponse> update(@Valid @RequestBody ProductRequest request, @PathVariable("id") Long id);

    @Operation(summary = "Deleta um produto pelo id.", method = "DELETE")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductResponse.class)) }, description = "Requisição com sucesso"),
            @ApiResponse(responseCode = "404", content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionProblem.class)) }, description = "O recurso não foi encontrado") })
    ResponseEntity<Void> delete(@PathVariable("id") Long id);
}
