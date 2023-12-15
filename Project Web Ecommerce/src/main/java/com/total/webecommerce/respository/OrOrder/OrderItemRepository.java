package com.total.webecommerce.respository.OrOrder;

import com.total.webecommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {


    @Override
    void deleteById(Integer integer);

    @Query(nativeQuery = true,value = "SELECT * from order_item a where a.order_bill_id is null")
    List<OrderItem> findOrderItemIsNull();

    @Transactional
    @Modifying
    void deleteAllByIdIn(Collection<Integer> ids);
}