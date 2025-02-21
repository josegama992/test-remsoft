package com.br.remsoft.product.service.impl;

import com.br.remsoft.product.dto.request.ProductRequest;
import com.br.remsoft.product.dto.response.ProductResponse;
import com.br.remsoft.product.exception.BaseException;
import com.br.remsoft.product.mapper.ProductMapper;
import com.br.remsoft.product.model.Product;
import com.br.remsoft.product.repository.OrderProductRepository;
import com.br.remsoft.product.repository.ProductRepository;
import com.br.remsoft.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final OrderProductRepository orderProductRepository;
    private final ProductMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponse> list(Pageable pageable){
        Page<Product> model = repository.findAll(pageable);
        return model.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse findById(Long id){
        Product model = find(id);
        return mapper.toResponse(model);
    }

    @Transactional
    @Override
    public ProductResponse create(ProductRequest request){
       Product model = mapper.toModel(request);
       model = repository.save(model);
       return mapper.toResponse(model);
    }

    @Transactional
    @Override
    public ProductResponse update(ProductRequest request, Long id){
        Product model = find(id);
        BeanUtils.copyProperties(request, model);
        model = repository.save(model);
        return mapper.toResponse(model);
    }

    @Transactional
    @Override
    public void delete(Long id){
        find(id);
        if(orderProductRepository.existsByProductId(id)){
            throw new BaseException(HttpStatus.BAD_REQUEST, String.format("Produto de id %s já está em uso.", id));
        }
        repository.deleteById(id);
    }

    private Product find(Long id){
        Optional<Product> model = repository.findById(id);
        if(model.isEmpty()){
            throw new BaseException(HttpStatus.NOT_FOUND, String.format("Produto de id %s não encontrado.", id));
        }
        return model.get();
    }
}
