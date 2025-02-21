package com.br.remsoft.product.service.impl;

import com.br.remsoft.product.dto.request.OrderProductRequest;
import com.br.remsoft.product.dto.request.OrderRequest;
import com.br.remsoft.product.dto.response.OrderResponse;
import com.br.remsoft.product.exception.BaseException;
import com.br.remsoft.product.mapper.OrderMapper;
import com.br.remsoft.product.mapper.OrderProductMapper;
import com.br.remsoft.product.model.Order;
import com.br.remsoft.product.model.OrderProduct;
import com.br.remsoft.product.model.Product;
import com.br.remsoft.product.repository.OrderProductRepository;
import com.br.remsoft.product.repository.OrderRepository;
import com.br.remsoft.product.repository.ProductRepository;
import com.br.remsoft.product.service.OrderService;
import com.br.remsoft.product.util.StreamUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderMapper mapper;
    private final OrderProductMapper orderProductMapper;

    @Transactional(readOnly = true)
    @Override
    public Page<OrderResponse> list(Pageable pageable){
        Page<Order> model = repository.findAll(pageable);
        return model.map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponse findById(Long id){
        Order model = find(id);
        return mapper.toResponse(model);
    }

    @Transactional
    @Override
    public OrderResponse create(OrderRequest request){
        Order model = mapper.toModel(request);
        model = repository.save(model);

        if(nonNull(request.getOrderProduct()) && !request.getOrderProduct().isEmpty()){
            Map<Long, Long> productValueMap = request.getOrderProduct().stream().collect(Collectors.toMap(OrderProductRequest::getProductId, OrderProductRequest::getAmmount));
            List<Product> productList = productRepository.findAllById(request.getOrderProduct().stream().map(OrderProductRequest::getProductId).toList());
            List<OrderProduct> orderProducts = orderProductMapper.toModel(productList, model, productValueMap);
            orderProducts = orderProductRepository.saveAll(orderProducts);
            model.setOrderProducts(orderProducts);
        }

        return mapper.toResponse(model);
    }

    @Transactional
    @Override
    public OrderResponse update(OrderRequest request, Long id){
        Order model = find(id);
        model = repository.save(model);

        if(nonNull(request.getOrderProduct()) && !request.getOrderProduct().isEmpty()){

            Map<Long, Long> productValueMap = request.getOrderProduct().stream().filter(StreamUtils.distinctByKeys(OrderProductRequest::getProductId)).collect(Collectors.toMap(OrderProductRequest::getProductId, OrderProductRequest::getAmmount));
            List<OrderProduct> productsToDelete = model.getOrderProducts().stream().filter(p -> !productValueMap.containsKey(p.getProduct().getId())).toList();
            if(!productsToDelete.isEmpty()){
                orderProductRepository.deleteAll(productsToDelete);
                model.getOrderProducts().removeAll(productsToDelete);
            }
            model.setOrderProducts(model.getOrderProducts().stream().filter(p -> productValueMap.containsKey(p.getProduct().getId()))
                    .map(m -> {
                        m.setAmmout(productValueMap.get(m.getProduct().getId()));
                        return m;
                        }).toList());

            Set<Long> existingProducts = model.getOrderProducts().stream().map(m -> m.getProduct().getId()).collect(Collectors.toSet());
            List<Long> productsToAddSet = productValueMap.keySet().stream().filter(p -> !existingProducts.contains(p)).toList();
            List<Product> productsToAdd = productRepository.findAllById(productsToAddSet);
            List<OrderProduct> orderProductToAdd = Stream.concat(orderProductMapper.toModel(productsToAdd, model, productValueMap).stream(), model.getOrderProducts().stream()).toList();
            model.setOrderProducts(orderProductToAdd);
            orderProductRepository.saveAll(model.getOrderProducts());
        }

        return mapper.toResponse(model);
    }

    @Transactional
    @Override
    public void delete(Long id){
        find(id);
        orderProductRepository.deleteByOrderId(id);
        repository.deleteById(id);
    }

    private Order find(Long id){
        Optional<Order> model = repository.findById(id);
        if(model.isEmpty()){
            throw new BaseException(HttpStatus.NOT_FOUND, String.format("Pedido de id %s não encontrado.", id));
        }
        return model.get();
    }
}
