package com.total.webecommerce.entity.projection.OfUser;

import com.total.webecommerce.entity.OrderBill;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Projection for {@link OrderBill}
 */
public interface OrderBillInfo {
    Integer getId();

    Double getTotalPrice();

    UserInfo getUser();

    Set<OrderItemInfo> getOrderItems();
    @RequiredArgsConstructor
    class OrderBillImpl implements OrderBillInfo{
        private final OrderBill orderBill;

        @Override
        public Integer getId() {
            return orderBill.getId();
        }

        @Override
        public Double getTotalPrice() {
            return orderBill.getTotalPrice();
        }



     @Override
     public UserInfo getUser(){
            return UserInfo.of(this.orderBill.getUser());
     }
        @Override
        public Set<OrderItemInfo> getOrderItems() {
            return orderBill.getOrderItems().stream().map(e->OrderItemInfo.of(e)).collect(Collectors.toSet());
        }
    }
}