package com.total.webecommerce.entity.projection.OfUser;

import com.total.webecommerce.entity.OrderBill;
import lombok.RequiredArgsConstructor;

/**
 * Projection for {@link OrderBill}
 */
public interface MyOrderInfo {
    Integer getId();

    @RequiredArgsConstructor
    class MyOrderInfoImpl implements MyOrderInfo{
        private final OrderBill orderBill;

        @Override
        public Integer getId() {
            return orderBill.getId();
        }
    }
    static MyOrderInfo of(OrderBill orderBill){
        return new MyOrderInfoImpl(orderBill);
    }
}