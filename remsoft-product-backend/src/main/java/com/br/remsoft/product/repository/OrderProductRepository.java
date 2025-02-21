package com.br.remsoft.product.repository;

import com.br.remsoft.product.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query("select COUNT(op.id) > 0 from OrderProduct op where op.product.id = :id")
    boolean existsByProductId(Long id);

    @Query("delete OrderProduct op where op.order.id = :id")
    @Modifying
    void deleteByOrderId(Long id);
}
